package maventest.service.impl;

import lombok.RequiredArgsConstructor;
import maventest.common.ResultCode;
import maventest.common.exception.ApiException;
import maventest.common.security.AppUserDetails;
import maventest.common.security.AppUserDetailsService;
import maventest.common.security.JwtUtil;
import maventest.dto.auth.CurrentUserResponse;
import maventest.dto.auth.LoginRequest;
import maventest.dto.auth.LoginResponseData;
import maventest.entity.CustomerInfo;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements maventest.service.AuthService {

    private final AppUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponseData login(LoginRequest request) {
        AppUserDetails userDetails;
        try {
            userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(request.getUsername());
        } catch (Exception e) {
            throw new ApiException(
                    String.valueOf(ResultCode.UNAUTHORIZED.getCode()),
                    "帳號或密碼錯誤",
                    HttpStatus.UNAUTHORIZED);
        }

        if (!passwordEncoder.matches(request.getPassword(), userDetails.getPassword())) {
            throw new ApiException(
                    String.valueOf(ResultCode.UNAUTHORIZED.getCode()),
                    "帳號或密碼錯誤",
                    HttpStatus.UNAUTHORIZED);
        }

        CustomerInfo customer = userDetails.getCustomer();
        List<String> roles = List.of("ROLE_USER");
        String token = jwtUtil.generateToken(customer.getUsername(), roles);

        return LoginResponseData.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .expiresIn(jwtUtil.getExpirationSeconds())
                .username(customer.getUsername())
                .displayName(customer.getName())
                .roles(roles)
                .build();
    }

    @Override
    public CurrentUserResponse currentUser(String username) {
        AppUserDetails userDetails = (AppUserDetails) userDetailsService.loadUserByUsername(username);
        CustomerInfo customer = userDetails.getCustomer();
        return CurrentUserResponse.builder()
                .username(customer.getUsername())
                .displayName(customer.getName())
                .roles(List.of("ROLE_USER"))
                .build();
    }
}
