package maventest.auth.service;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.common.security.JwtTokenProvider;
import maventest.auth.dto.AuthCurrentUserRespDto;
import maventest.auth.dto.AuthLoginReqDto;
import maventest.auth.dto.AuthLoginRespDto;
import maventest.auth.dto.AuthRegisterReqDto;
import maventest.auth.dto.AuthRegisterRespDto;
import maventest.auth.dto.AuthUpdateReqDto;
import maventest.auth.dto.AuthUserRespDto;
import maventest.auth.entity.AppUserEntity;
import maventest.auth.entity.AppUserPrincipal;
import maventest.auth.repository.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthLoginRespDto login(AuthLoginReqDto reqDto) {
        AppUserEntity user = appUserRepository.findByUsername(reqDto.getUsername())
                .orElseThrow(() -> new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED));

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ApiException(ApiCode.USER_DISABLED.getCode(), ApiCode.USER_DISABLED.getMessage(), HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(reqDto.getPassword(), user.getPassword())) {
            throw new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        System.out.println("login:" + reqDto.getUsername());
        return AuthLoginRespDto.builder()
                .accessToken(jwtTokenProvider.generateToken(user.getUsername(), user.getDisplayName(), user.getRoleCode()))
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationSeconds())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .roleCode(user.getRoleCode())
                .build();
    }

    public AuthCurrentUserRespDto currentUser(AppUserPrincipal principal) {
        return AuthCurrentUserRespDto.builder()
                .username(principal.getUsername())
                .displayName(principal.getDisplayName())
                .roleCode(principal.getRoleCode())
                .build();
    }

    public AuthRegisterRespDto register(AuthRegisterReqDto authRegisterReqDto) {
        if (appUserRepository.existsByUserName(authRegisterReqDto.getUsername())) {
            throw new ApiException(ApiCode.USERNAME_ALREADY_EXISTS.getCode(), ApiCode.USERNAME_ALREADY_EXISTS.getMessage(), HttpStatus.CONFLICT);
        }

        AppUserEntity newUser = AppUserEntity.builder()
                .username(authRegisterReqDto.getUsername())
                .password(passwordEncoder.encode(authRegisterReqDto.getPassword()))
                .displayName(authRegisterReqDto.getDisplayname())
                .roleCode("APPLICANT")
                .status("ACTIVE")
                .build();

        appUserRepository.Save(newUser);

        return AuthRegisterRespDto.builder()
                .username(authRegisterReqDto.getUsername())
                .displayName(authRegisterReqDto.getDisplayname())
                .roleCode("APPLICANT")
                .build();
    }

    public List<AuthUserRespDto> findAll() {
        return appUserRepository.findAll()
            .stream()
            .map(user -> AuthUserRespDto.builder()
                    .username(user.getUsername())
                    .displayName(user.getDisplayName())
                    .roleCode(user.getRoleCode())
                    .status(user.getStatus())
                    .build())
            .toList();
    }

    public void updateUser(String username, AuthUpdateReqDto reqDto) {
        AppUserEntity existingUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException(ApiCode.APPLICATION_NOT_FOUND.getCode(),
                    ApiCode.APPLICATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));

        AppUserEntity updateUser = AppUserEntity.builder()
            .username(username)
            .password(reqDto.getPassword() != null && !reqDto.getPassword().isBlank()
                ? passwordEncoder.encode(reqDto.getPassword())
                : existingUser.getPassword())
            .displayName(reqDto.getDisplayname() != null && !reqDto.getDisplayname().isBlank()
                ? reqDto.getDisplayname()
                : existingUser.getDisplayName())
            .status(reqDto.getStatus() != null && !reqDto.getStatus().isBlank()
                ? reqDto.getStatus()
                : existingUser.getStatus())
            .roleCode(reqDto.getRole() != null && !reqDto.getRole().isBlank()
                ? reqDto.getRole()
                : existingUser.getRoleCode())
            .build();

        appUserRepository.updateByUserName(updateUser);

        System.out.println("updateUser called: " + username);
        System.out.println("status: " + reqDto.getStatus());
        System.out.println("role: " + reqDto.getRole());
    }

    public void delectUser(String username) {
        appUserRepository.deleteUser(username);
    }
}
