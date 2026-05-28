package com.example.demo.service;

import com.example.demo.config.JwtProperties;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private final SecretKey secretKey;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        Instant expireAt = now.plusMillis(jwtProperties.accessTokenExpireMs());

        return Jwts.builder()
                .subject(String.valueOf(user.getUser_id()))
                .claims(Map.of(
                        "type", "access",
                        "loginId", user.getUser_login_id(),
                        "nickname", user.getUser_nickname()
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant expireAt = now.plusMillis(jwtProperties.refreshTokenExpireMs());

        return Jwts.builder()
                .subject(String.valueOf(user.getUser_id()))
                .claims(Map.of(
                        "type", "refresh",
                        "loginId", user.getUser_login_id()
                ))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expireAt))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            throw new AuthException("유효하지 않거나 만료된 토큰입니다.");
        }
    }

    public Claims parseAccessToken(String token) {
        Claims claims = parseToken(token);
        String type = claims.get("type", String.class);
        if (!"access".equals(type)) {
            throw new AuthException("Access 토큰이 아닙니다.");
        }
        return claims;
    }

    public Claims parseRefreshToken(String token) {
        Claims claims = parseToken(token);
        String type = claims.get("type", String.class);
        if (!"refresh".equals(type)) {
            throw new AuthException("Refresh 토큰이 아닙니다.");
        }
        return claims;
    }
}
