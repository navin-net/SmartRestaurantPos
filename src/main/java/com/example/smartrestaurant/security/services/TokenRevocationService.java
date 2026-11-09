package com.example.smartrestaurant.security.services;

import com.example.smartrestaurant.model.RevokedToken;
import com.example.smartrestaurant.repositories.RevokedTokenRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;

@Service
public class TokenRevocationService {
    private final RevokedTokenRepository repository;

    public TokenRevocationService(RevokedTokenRepository repository) {
        this.repository = repository;
    }

    public boolean isRevoked(String token) {
        return repository.existsByTokenHashAndExpiresAtAfter(hash(token), Instant.now());
    }

    public void revoke(String token, Instant expiresAt) {
        String hash = hash(token);
        if (repository.existsByTokenHashAndExpiresAtAfter(hash, Instant.now())) {
            return;
        }
        RevokedToken revoked = new RevokedToken();
        revoked.setTokenHash(hash);
        revoked.setExpiresAt(expiresAt);
        try {
            repository.save(revoked);
        } catch (DataIntegrityViolationException ignored) {
            // a concurrent logout already stored it
        }
    }

    @Scheduled(fixedRate = 3_600_000)
    public void cleanup() {
        repository.deleteExpired(Instant.now());
    }

    private static String hash(String token) {
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256")
                    .digest(token.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}