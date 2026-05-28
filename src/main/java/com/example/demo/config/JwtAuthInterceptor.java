package com.example.demo.config;

import com.example.demo.exception.AuthException;
import com.example.demo.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtAuthInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;

    public JwtAuthInterceptor(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException("Authorization Bearer 토큰이 필요합니다.");
        }

        String token = authorization.substring(7);
        Claims claims = jwtService.parseAccessToken(token);
        request.setAttribute("auth_user_id", Long.parseLong(claims.getSubject()));
        request.setAttribute("auth_user_login_id", claims.get("loginId", String.class));
        return true;
    }
}
