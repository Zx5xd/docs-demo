package com.example.demo.service;

import com.example.demo.config.JwtProperties;
import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.LoginResponseDTO;
import com.example.demo.dto.ForgotPasswordRequestDTO;
import com.example.demo.dto.ForgotPasswordResponseDTO;
import com.example.demo.dto.SignupRequestDTO;
import com.example.demo.dto.TokenRefreshRequestDTO;
import com.example.demo.entity.RefreshToken;
import com.example.demo.entity.User;
import com.example.demo.exception.AuthException;
import com.example.demo.repository.RefreshTokenRepository;
import com.example.demo.repository.UserRepository;
import io.jsonwebtoken.Claims;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.security.SecureRandom;

@Service
@Transactional
public class AuthService {
    private static final String TEMP_PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$";
    private static final int TEMP_PASSWORD_LENGTH = 12;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final JwtProperties jwtProperties;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtService jwtService, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.jwtProperties = jwtProperties;
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        User user = userRepository.findByUser_login_id(request.getUser_login_id())
                .orElseThrow(() -> new AuthException("아이디 또는 비밀번호가 올바르지 않습니다."));

        if (!isBcryptHash(user.getUser_password())) {
            throw new AuthException("해당 계정 비밀번호 정책이 올바르지 않습니다. 비밀번호 재설정이 필요합니다.");
        }

        if (!passwordEncoder.matches(request.getUser_password(), user.getUser_password())) {
            throw new AuthException("아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUser_id(user.getUser_id());
        response.setUser_login_id(user.getUser_login_id());
        response.setUser_nickname(user.getUser_nickname());
        response.setUser_email(user.getUser_email());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user, refreshToken);

        response.setAccess_token(accessToken);
        response.setRefresh_token(refreshToken);
        response.setToken_type("Bearer");
        response.setAccess_expires_in(jwtProperties.accessTokenExpireMs() / 1000);
        response.setRefresh_expires_in(jwtProperties.refreshTokenExpireMs() / 1000);
        response.setMessage("로그인 성공");
        return response;
    }

    public void signup(SignupRequestDTO request) {
        if (userRepository.existsByUser_login_id(request.getUser_login_id())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
        if (userRepository.existsByUser_email(request.getUser_email())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        User user = new User();
        user.setUser_login_id(request.getUser_login_id());
        user.setUser_password(passwordEncoder.encode(request.getUser_password()));
        user.setUser_nickname(request.getUser_nickname());
        user.setUser_email(normalizeEmail(request.getUser_email()));
        user.setUser_createdAt((int) LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond());

        userRepository.save(user);
    }

    public ForgotPasswordResponseDTO forgotPasswordByEmail(ForgotPasswordRequestDTO request) {
        User user = userRepository.findByUser_email(request.getUser_email())
                .orElseThrow(() -> new IllegalArgumentException("등록된 이메일이 없습니다."));

        String tempPassword = generateTempPassword();
        user.setUser_password(passwordEncoder.encode(tempPassword));

        ForgotPasswordResponseDTO response = new ForgotPasswordResponseDTO();
        response.setMessage("임시 비밀번호를 이메일로 발급했습니다.");
        response.setTemp_password(tempPassword);
        return response;
    }

    public LoginResponseDTO refresh(TokenRefreshRequestDTO request) {
        Claims claims = jwtService.parseRefreshToken(request.getRefresh_token());
        long userId = Long.parseLong(claims.getSubject());

        RefreshToken stored = refreshTokenRepository
                .findByRefresh_tokenAndRevokedFalse(request.getRefresh_token())
                .orElseThrow(() -> new AuthException("유효한 Refresh 토큰이 아닙니다."));

        if (stored.getRefresh_expire_at() < Instant.now().toEpochMilli()) {
            stored.setRevoked(true);
            throw new AuthException("Refresh 토큰이 만료되었습니다.");
        }

        if (stored.getUser().getUser_id() != userId) {
            throw new AuthException("토큰 사용자 정보가 일치하지 않습니다.");
        }

        User user = stored.getUser();
        stored.setRevoked(true);

        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        saveRefreshToken(user, newRefreshToken);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setUser_id(user.getUser_id());
        response.setUser_login_id(user.getUser_login_id());
        response.setUser_nickname(user.getUser_nickname());
        response.setUser_email(user.getUser_email());
        response.setAccess_token(newAccessToken);
        response.setRefresh_token(newRefreshToken);
        response.setToken_type("Bearer");
        response.setAccess_expires_in(jwtProperties.accessTokenExpireMs() / 1000);
        response.setRefresh_expires_in(jwtProperties.refreshTokenExpireMs() / 1000);
        response.setMessage("토큰 재발급 성공");
        return response;
    }

    public void logout(TokenRefreshRequestDTO request) {
        refreshTokenRepository.findByRefresh_tokenAndRevokedFalse(request.getRefresh_token())
                .ifPresent(token -> token.setRevoked(true));
    }

    private void saveRefreshToken(User user, String token) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setRefresh_token(token);
        refreshToken.setRefresh_expire_at(Instant.now().plusMillis(jwtProperties.refreshTokenExpireMs()).toEpochMilli());
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);
    }

    private boolean isBcryptHash(String password) {
        return password != null && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    private String normalizeEmail(String email) {
        return email;
    }

    private String generateTempPassword() {
        StringBuilder builder = new StringBuilder(TEMP_PASSWORD_LENGTH);
        for (int i = 0; i < TEMP_PASSWORD_LENGTH; i++) {
            builder.append(TEMP_PASSWORD_CHARS.charAt(SECURE_RANDOM.nextInt(TEMP_PASSWORD_CHARS.length())));
        }
        return builder.toString();
    }
}
