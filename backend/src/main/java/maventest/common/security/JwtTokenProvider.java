package maventest.common.security;

import maventest.auth.entity.AppUserPrincipal;
<<<<<<< HEAD
import maventest.common.ApiCode;
=======
import maventest.code.ApiCode;
>>>>>>> develop
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

    public String generateToken(String username, String displayName, String roleCode) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(expirationSeconds);
        return Jwts.builder()
                .subject(username)
                .claim("displayName", displayName)
                .claim("roleCode", roleCode)
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
        String roleCode = claims.get("roleCode", String.class);
        AppUserPrincipal principal = new AppUserPrincipal(username, displayName, roleCode);
        Collection<SimpleGrantedAuthority> authorities = roleCode != null
                ? List.of(new SimpleGrantedAuthority("ROLE_" + roleCode))
                : List.of();
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
