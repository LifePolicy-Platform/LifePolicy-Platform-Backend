package maventest.common.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import maventest.auth.entity.AppUserPrincipal;

/**
 * 從 SecurityContext 解析業務角色（APPLICANT / REVIEWER / ADMIN）。
 * 支援 AppUserPrincipal（auth JWT）與舊版 JwtUtil 的 ROLE_ 權限。
 */
public final class SecurityContextRoleResolver {

    private SecurityContextRoleResolver() {
    }

    public static String resolveRoleCode() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof AppUserPrincipal appUserPrincipal) {
            return normalizeRoleCode(appUserPrincipal.getRoleCode());
        }

        String roleFromAuthority = resolveRoleFromAuthorities(authentication);
        if (roleFromAuthority != null) {
            return roleFromAuthority;
        }

        return null;
    }

    /**
     * 嘗試以 auth JWT（roleCode claim）建立 Authentication；失敗則回傳 null，由舊版解析器接手。
     */
    public static Authentication tryAuthenticateWithAuthJwt(String token, JwtTokenProvider jwtTokenProvider) {
        if (!StringUtils.hasText(token)) {
            return null;
        }
        try {
            jwtTokenProvider.validateToken(token);
            return jwtTokenProvider.getAuthentication(token);
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String resolveRoleFromAuthorities(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            String role = normalizeRoleCode(authority.getAuthority());
            if (role != null && !"USER".equals(role)) {
                return role;
            }
        }
        return null;
    }

    private static String normalizeRoleCode(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        String normalized = raw.trim().toUpperCase();
        if (normalized.startsWith("ROLE_")) {
            normalized = normalized.substring(5);
        }
        return switch (normalized) {
            case "APPLICANT", "REVIEWER", "ADMIN" -> normalized;
            default -> null;
        };
    }
}
