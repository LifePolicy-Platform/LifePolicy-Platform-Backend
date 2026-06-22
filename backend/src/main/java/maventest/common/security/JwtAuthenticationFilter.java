package maventest.common.security;
// ⬇️ 補上這幾行 Java 標準庫與 JJWT 依賴
import java.util.List;
import java.util.stream.Collectors;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, JwtTokenProvider jwtTokenProvider) {
        this.jwtUtil = jwtUtil;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        if (StringUtils.hasText(token)) {
            Authentication authentication = SecurityContextRoleResolver.tryAuthenticateWithAuthJwt(token, jwtTokenProvider);
            if (authentication == null) {
                authentication = tryAuthenticateWithLegacyJwt(token);
            }
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

    private Authentication tryAuthenticateWithLegacyJwt(String token) {
        try {
            Claims claims = jwtUtil.parseToken(token);
            String username = jwtUtil.getUsername(claims);
            List<String> roles = jwtUtil.getRoles(claims);

            List<SimpleGrantedAuthority> authorities = roles == null
                    ? List.of(new SimpleGrantedAuthority("ROLE_USER"))
                    : roles.stream()
                    .map(role -> new SimpleGrantedAuthority(
                            role.startsWith("ROLE_") ? role : "ROLE_" + role))
                    .collect(Collectors.toList());

            return new UsernamePasswordAuthenticationToken(username, null, authorities);
        } catch (JwtException | IllegalArgumentException ignored) {
            return null;
        }
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
