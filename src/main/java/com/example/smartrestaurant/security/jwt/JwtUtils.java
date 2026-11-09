package com.example.smartrestaurant.security.jwt;

import com.example.smartrestaurant.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {
    public static final String TOKEN_VERSION_CLAIM = "tv";

    @Value("${security.jwt.secret-key}")
    private String secret;
    @Getter
    @Value("${security.jwt.expiration-time:1800000}")
    private long expirationMs;

    private SecretKey signingKey() {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("security.jwt.secret-key must contain at least 32 UTF-8 bytes");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Authentication authentication) {
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        int tokenVersion = principal instanceof UserDetailsImpl details ? details.getTokenVersion() : 0;
        Date now = new Date();
        return Jwts.builder()
                .subject(principal.getUsername())
                .claim(TOKEN_VERSION_CLAIM, tokenVersion)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(signingKey())
                .compact();
    }

    /** Verifies signature and expiry once; returns empty if the token is invalid. */
    public Optional<Claims> parseClaims(String token) {
        try {
            return Optional.of(Jwts.parser().verifyWith(signingKey()).build()
                    .parseSignedClaims(token).getPayload());
        } catch (JwtException | IllegalArgumentException exception) {
            return Optional.empty();
        }
    }
}