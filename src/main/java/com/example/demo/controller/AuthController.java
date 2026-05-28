package com.example.demo.controller;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.ForgotPasswordRequestDTO;
import com.example.demo.dto.ForgotPasswordResponseDTO;
import com.example.demo.dto.SignupRequestDTO;
import com.example.demo.dto.TokenRefreshRequestDTO;
import com.example.demo.exception.AuthException;
import com.example.demo.service.AuthService;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@Valid @RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signup(@Valid @RequestBody SignupRequestDTO request) {
        authService.signup(request);
        return ResponseEntity.ok(Map.of("message", "회원가입 성공"));
    }

    @PostMapping("/forgot-password")
    public ForgotPasswordResponseDTO forgotPassword(@Valid @RequestBody ForgotPasswordRequestDTO request) {
        return authService.forgotPasswordByEmail(request);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Valid @RequestBody TokenRefreshRequestDTO request) {
        authService.logout(request);
        return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
    }

    @PostMapping("/refresh")
    public LoginResponseDTO refresh(@Valid @RequestBody TokenRefreshRequestDTO request) {
        return authService.refresh(request);
    }

    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> me(@RequestHeader(value = "Authorization", required = false) String authorization) {
        String token = extractBearerToken(authorization);
        Claims claims = jwtService.parseAccessToken(token);
        long userId = Long.parseLong(claims.getSubject());
        String loginId = claims.get("loginId", String.class);
        String nickname = claims.get("nickname", String.class);

        return ResponseEntity.ok(Map.of(
                "user_id", userId,
                "user_login_id", loginId,
                "user_nickname", nickname
        ));
    }

    private String extractBearerToken(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException("Authorization Bearer 토큰이 필요합니다.");
        }
        return authorization.substring(7);
    }
}
