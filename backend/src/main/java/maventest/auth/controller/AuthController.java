package maventest.auth.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import maventest.common.EntityUtil;
import maventest.common.ReturnMsg;
import maventest.auth.dto.AuthCurrentUserRespDto;
import maventest.auth.dto.AuthLoginReqDto;
import maventest.auth.dto.AuthLoginRespDto;
import maventest.auth.dto.AuthRegisterReqDto;
import maventest.auth.dto.AuthRegisterRespDto;
import maventest.auth.dto.AuthUpdateReqDto;
import maventest.auth.dto.AuthUserRespDto;
import maventest.auth.entity.AppUserPrincipal;
import maventest.auth.service.AuthService;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Authentication", description = "JWT login and current-user session APIs")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login and receive JWT access token",
            description = "Public endpoint. Submit USERNAME and PASSWORD, then use the returned ACCESS_TOKEN as Bearer token for protected APIs.",
            security = {}
    )
    public ResponseEntity<ReturnMsg<AuthLoginRespDto>> login(@Valid @RequestBody AuthLoginReqDto reqDto, BindingResult bindingResult) {
        EntityUtil.validDto(bindingResult);
        return ResponseEntity.ok(ReturnMsg.success(authService.login(reqDto)));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current authenticated user",
            description = "Protected endpoint. Returns username, display name, and roles restored from the current Bearer token.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<AuthCurrentUserRespDto>> me(Authentication authentication) {
        AppUserPrincipal principal = (AppUserPrincipal) authentication.getPrincipal();
        return ResponseEntity.ok(ReturnMsg.success(authService.currentUser(principal)));
    }

    @PostMapping("/register")
    @Operation(
        summary = "Register a new applicant account",
        description = "Protected endpoint. Only REVIEWER can register new APPLICANT accounts.",
        security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<AuthRegisterRespDto>> register(@Valid @RequestBody AuthRegisterReqDto authRegisterReqDto, BindingResult bindingResult) {
        EntityUtil.validDto(bindingResult);
        return ResponseEntity.ok(ReturnMsg.success(authService.register(authRegisterReqDto)));
    }


    @GetMapping("/users")
    @Operation(
            summary = "Get all users",
            description = "Protected endpoint. Only REVIEWER can get all users.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<List<AuthUserRespDto>>> findAll() {
        return ResponseEntity.ok(ReturnMsg.success(authService.findAll()));
    }

    @PutMapping("/user/{username}")
    @Operation(
            summary = "Update users",
            description = "Protected endpoint. Only REVIEWER can get all users.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<Void>> updateUser(@PathVariable String username, @RequestBody AuthUpdateReqDto reqDto, BindingResult bindingResult) {
        EntityUtil.validDto(bindingResult);
        authService.updateUser(username, reqDto);

        return ResponseEntity.ok(ReturnMsg.success(null));
    }

    @DeleteMapping("/user/{username}")
    @Operation(
            summary = "Delete users",
            description = "Protected endpoint. Only REVIEWER can delete users.",
            security = {@SecurityRequirement(name = "bearerAuth")}
    )
    public ResponseEntity<ReturnMsg<Void>> delectUser(@PathVariable String username) {
        authService.delectUser(username);
        return ResponseEntity.ok(ReturnMsg.success(null));
    }
}
