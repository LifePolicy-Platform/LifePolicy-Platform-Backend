package maventest.service;  


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import maventest.common.security.JwtTokenProvider;
import maventest.dto.auth.AuthCurrentUserRespDto;
import maventest.dto.auth.AuthLoginReqDto;
import maventest.dto.auth.AuthLoginRespDto;
import maventest.dto.auth.AuthRegisterReqDto;
import maventest.dto.auth.AuthRegisterRespDto;
import maventest.dto.auth.AuthUpdateReqDto;
import maventest.dto.auth.AuthUserRespDto;
import maventest.entity.AppUserEntity;
import maventest.entity.AppUserPrincipal;
import maventest.service.impl.AppUserRepository;

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

        if (!user.isEnabled()) {
            throw new ApiException(ApiCode.USER_DISABLED.getCode(), ApiCode.USER_DISABLED.getMessage(), HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(reqDto.getPassword(), user.getPasswordHash())) {
            throw new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        System.out.println("login:" + reqDto.getUsername());
        return AuthLoginRespDto.builder()
                .accessToken(jwtTokenProvider.generateToken(user.getUsername(), user.getDisplayName(), user.getRoles()))
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationSeconds())
                .username(user.getUsername())
                .displayName(user.getDisplayName())
                .roles(user.getRoles())
                .build();
    }

    public AuthCurrentUserRespDto currentUser(AppUserPrincipal principal) {
        return AuthCurrentUserRespDto.builder()
                .username(principal.getUsername())
                .displayName(principal.getDisplayName())
                .roles(principal.getRoles())
                .build();
    }

    public AuthRegisterRespDto register(AuthRegisterReqDto authRegisterReqDto){
        if(appUserRepository.existsByUserName(authRegisterReqDto.getUsername())){
            throw new ApiException(ApiCode.USERNAME_ALREADY_EXISTS.getCode(), ApiCode.USERNAME_ALREADY_EXISTS.getMessage(), HttpStatus.CONFLICT);
        }

        AppUserEntity newUser = AppUserEntity.builder()
                .username(authRegisterReqDto.getUsername())
                .passwordHash(passwordEncoder.encode(authRegisterReqDto.getPassword()))
                .displayName(authRegisterReqDto.getDisplayname())
                .enabled(true)
                .build();

        Long userId = appUserRepository.Save(newUser);

        appUserRepository.Saverole(userId, "APPLICANT");

        return AuthRegisterRespDto.builder()
                .username(authRegisterReqDto.getUsername())
                .displayName(authRegisterReqDto.getDisplayname())
                .roles("APPLICANT")
                .build();
    }

    public List<AuthUserRespDto> findAll() {
        return appUserRepository.findAll()
            .stream()
            .map(user -> AuthUserRespDto.builder()
                    .username(user.getUsername())
                    .displayName(user.getDisplayName())
                    .roles(user.getRoles())
                    .enabled(user.isEnabled())
                    .build())
            .toList();
    }

    public void updateUser(String username, AuthUpdateReqDto reqDto){
         AppUserEntity existingUser = appUserRepository.findByUsername(username)
            .orElseThrow(() -> new ApiException(ApiCode.APPLICATION_NOT_FOUND.getCode(),
                    ApiCode.APPLICATION_NOT_FOUND.getMessage(), HttpStatus.NOT_FOUND));
        
        AppUserEntity updateUser = AppUserEntity.builder()
            .username(username)
            .passwordHash(reqDto.getPassword() != null && !reqDto.getPassword().isBlank()
                ? passwordEncoder.encode(reqDto.getPassword())  // 有填才更新密碼
                : existingUser.getPasswordHash())               // 沒填就保留原本
            .displayName(reqDto.getDisplayname() != null && !reqDto.getDisplayname().isBlank()
                ? reqDto.getDisplayname()         
                : existingUser.getDisplayName())   
            .enabled(reqDto.isEnable())
            .build();

        appUserRepository.updateByUserName(updateUser);

        if (reqDto.getRole() != null && !reqDto.getRole().isBlank()) {
            appUserRepository.delectUserRole(username);
            appUserRepository.saveRoleByUserName(username, reqDto.getRole());
        }

        System.out.println("updateUser called: " + username);
        System.out.println("enabled: " + reqDto.isEnable());
        System.out.println("role: " + reqDto.getRole());
            
    }

    public void delectUser(String username){
        appUserRepository.delectUserRole(username);
        appUserRepository.deleteUser(username);
    }
}