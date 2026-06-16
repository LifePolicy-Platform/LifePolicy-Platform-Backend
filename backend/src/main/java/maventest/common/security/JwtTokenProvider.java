package maventest.common.security;

import maventest.entity.AppUserPrincipal;
import maventest.code.ApiCode;
import maventest.common.exception.ApiException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationSeconds;

    public JwtTokenProvider(@Value("${auth.jwt.secret}") String secret, @Value("${auth.jwt.expiration-seconds}") long expirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(decodeSecret(secret));
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String username, String displayName, List<String> roles) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirationSeconds);
        return Jwts.builder()
                .subject(username)
                .claim("displayName", displayName)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(secretKey)
                .compact();
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (Exception exception) {
            throw new ApiException(ApiCode.TOKEN_INVALID.getCode(), ApiCode.TOKEN_INVALID.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String username = claims.getSubject();
        String displayName = claims.get("displayName", String.class);
        List<?> rolesClaim = claims.get("roles", List.class);
        List<String> roles = rolesClaim == null
            ? List.of()
            : rolesClaim.stream().map(String::valueOf).toList();
        AppUserPrincipal principal = new AppUserPrincipal(username, displayName, roles);
        Collection<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList();
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private byte[] decodeSecret(String secret) {
        try {
            return Decoders.BASE64.decode(secret);
        } catch (Exception ignored) {
            return secret.getBytes(StandardCharsets.UTF_8);
        }
    }
}