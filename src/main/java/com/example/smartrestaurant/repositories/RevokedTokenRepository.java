package com.example.smartrestaurant.repositories;

import com.example.smartrestaurant.model.RevokedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Repository
public interface RevokedTokenRepository extends JpaRepository<RevokedToken, Long> {
    boolean existsByTokenHashAndExpiresAtAfter(String tokenHash, Instant now);

    @Modifying
    @Transactional
    @Query("delete from RevokedToken r where r.expiresAt < :now")
    void deleteExpired(@Param("now") Instant now);
}