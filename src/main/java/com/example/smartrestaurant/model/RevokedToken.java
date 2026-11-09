package com.example.smartrestaurant.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "revoked_tokens")
@Getter @Setter @NoArgsConstructor
public class RevokedToken {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_hash", nullable = false, unique = true, length = 64)
    private String tokenHash;     // SHA-256 of the token, never the token itself

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;
}