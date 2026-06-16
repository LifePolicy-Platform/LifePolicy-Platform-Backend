package maventest.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationSeconds;

    public JwtUtil(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.expiration:86400}") long expirationSeconds) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = expirationSeconds;
    }

    /** 產生 JWT */
    public String generateToken(String username, List<String> roles) {
        long nowMs = System.currentTimeMillis();
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(new Date(nowMs))
                .expiration(new Date(nowMs + expirationSeconds * 1000))
                .signWith(secretKey)
                .compact();
    }

    /** 解析並驗證 JWT，回傳 Claims */
    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** 從 Claims 取得 username */
    public String getUsername(Claims claims) {
        return claims.getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles", List.class);
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}
