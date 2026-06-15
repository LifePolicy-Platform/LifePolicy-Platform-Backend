package maventest.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import maventest.common.config.OpenApiConfig;
import maventest.dto.auth.AuthApiResponse;
import maventest.dto.auth.CurrentUserResponse;
import maventest.dto.auth.LoginRequest;
import maventest.dto.auth.LoginResponseData;
import maventest.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "登入驗證", description = "JWT 登入、取得目前使用者（Token 由前端清除以登出）")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @Operation(summary = "登入", description = "驗證帳密後回傳 access token，前端請以 Bearer 帶入後續 API")
    @PostMapping("/login")
    public ResponseEntity<AuthApiResponse<LoginResponseData>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponseData data = authService.login(request);
        return ResponseEntity.ok(AuthApiResponse.ok(data));
    }

    @Operation(
        summary = "目前登入使用者",
        description = "需帶 Authorization: Bearer {accessToken}",
        security = { @SecurityRequirement(name = OpenApiConfig.BEARER_SCHEME) }
    )
    @GetMapping("/me")
    public ResponseEntity<AuthApiResponse<CurrentUserResponse>> me(Authentication authentication) {
        String username = (String) authentication.getPrincipal();
        CurrentUserResponse data = authService.currentUser(username);
        return ResponseEntity.ok(AuthApiResponse.ok(data));
    }
}
