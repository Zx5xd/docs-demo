package com.example.demo.repository;

import com.example.demo.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Query("SELECT r FROM RefreshToken r WHERE r.refresh_token = :token AND r.revoked = false")
    Optional<RefreshToken> findByRefresh_tokenAndRevokedFalse(@Param("token") String refreshToken);
}
