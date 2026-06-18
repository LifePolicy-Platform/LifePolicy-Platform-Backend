package maventest.service;


import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import maventest.entity.CustomerInfo;
import maventest.mapper.CustomerInfoMapper;
import maventest.service.impl.AppUserRepository;

@Service
public class AuthService {

    private final AppUserRepository appUserRepository;
    private final CustomerInfoMapper customerInfoMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            AppUserRepository appUserRepository,
            CustomerInfoMapper customerInfoMapper,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider) {
        this.appUserRepository = appUserRepository;
        this.customerInfoMapper = customerInfoMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public AuthLoginRespDto login(AuthLoginReqDto reqDto) {
        return appUserRepository.findByUsername(reqDto.getUsername())
                .filter(AppUserEntity::isEnabled)
                .map(user -> loginAppUser(user, reqDto.getPassword()))
                .orElseGet(() -> loginCustomerUser(reqDto));
    }

    private AuthLoginRespDto loginAppUser(AppUserEntity user, String rawPassword) {
        if (!user.isEnabled()) {
            throw new ApiException(
                    ApiCode.USER_DISABLED.getCode(),
                    ApiCode.USER_DISABLED.getMessage() + " (STATUS=" + user.getStatus() + ")",
                    HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return buildLoginResponse(user.getUsername(), user.getDisplayName(), user.getRoles());
    }

    private AuthLoginRespDto loginCustomerUser(AuthLoginReqDto reqDto) {
        CustomerInfo customer = customerInfoMapper.selectOne(
                new LambdaQueryWrapper<CustomerInfo>()
                        .eq(CustomerInfo::getUsername, reqDto.getUsername()));

        if (customer == null) {
            throw new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }
        if (!customer.isEnabled()) {
            throw new ApiException(
                    ApiCode.USER_DISABLED.getCode(),
                    ApiCode.USER_DISABLED.getMessage() + " (STATUS=" + customer.getStatus() + ")",
                    HttpStatus.FORBIDDEN);
        }
        if (!passwordEncoder.matches(reqDto.getPassword(), customer.getPassword())) {
            throw new ApiException(ApiCode.INVALID_CREDENTIALS.getCode(), ApiCode.INVALID_CREDENTIALS.getMessage(), HttpStatus.UNAUTHORIZED);
        }

        return buildLoginResponse(customer.getUsername(), customer.getUsername(), List.of("USER"));
    }

    private AuthLoginRespDto buildLoginResponse(String username, String displayName, List<String> roles) {
        return AuthLoginRespDto.builder()
                .accessToken(jwtTokenProvider.generateToken(username, displayName, roles))
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationSeconds())
                .username(username)
                .displayName(displayName)
                .roles(roles)
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
                .password(passwordEncoder.encode(authRegisterReqDto.getPassword()))
                .displayName(authRegisterReqDto.getDisplayname())
                .roleCode("APPLICANT")
                .status("ACTIVE")
                .roleCode("APPLICANT")
                .status("ACTIVE")
                .build();

        appUserRepository.Save(newUser);
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
