package alura.forumhub.api.infra.security;

import alura.forumhub.api.infra.exception.TokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Clock;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

@Component
public class JwtUtils {

    private final long jwtExpirationMs;
    private final SecretKey signingKey;
    private final Clock clock;

    public JwtUtils(
            @Value("${jwt.secret:umsegredomuitoseguro}")
            String jwtSecret,
            @Value("${jwt.expiration:3600000}")
            long jwtExpirationMs,
            Clock clock
    ) throws TokenException {
        this.jwtExpirationMs = jwtExpirationMs;
        this.clock = clock;
        this.signingKey = getSigningKey(jwtSecret);
    }

    public SecretKeySpec getSigningKey(String jwtSecret) {
        try {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            if (keyBytes.length < 32) {
                MessageDigest digest = MessageDigest.getInstance("SHA-256");
                keyBytes = Arrays.copyOf(digest.digest(keyBytes), 32);
            }
            return new SecretKeySpec(keyBytes, "HmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            throw new TokenException("Falha ao inicializar chave JWT: " + e.getMessage());
        }
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl usuarioPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return generateTokenFromEmail(usuarioPrincipal.getUsername());
    }

    public String generateTokenFromEmail(String email) {
        Instant now = clock.instant();
        Date issuedAt = Date.from(now);
        Date expiration = Date.from(now.plusMillis(jwtExpirationMs));

        return Jwts.builder()
                .subject(email)
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(signingKey)
                .compact();
    }

    public String getEmailFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public void validateJwtToken(String authToken) {
        if (authToken == null || authToken.isBlank()) {
            throw new TokenException("Token JWT está vazio");
        }

        try {
            Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(authToken);
        } catch (SecurityException e) {
            throw new TokenException("Assinatura JWT inválida: " + e.getMessage());
        } catch (MalformedJwtException e) {
            throw new TokenException("Token JWT inválido: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            throw new TokenException("Token JWT expirado: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            throw new TokenException("Token JWT não suportado: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new TokenException("JWT claims string está vazia: " + e.getMessage());
        }
    }
}