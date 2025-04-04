package io.goorm.route33.config;

import io.goorm.route33.auth.TokenService;
import io.goorm.route33.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final TokenService tokenService;
    private static final String AUTHENTICATION_TYPE = "Bearer ";
    private static final int BEARER_PREFIX_LENGTH = 7;

    public AuthInterceptor(TokenService tokenService) {
        this.tokenService = tokenService;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(AUTHORIZATION);
        if (header == null || !header.startsWith(AUTHENTICATION_TYPE)) {
            throw new CustomException("유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED);
        }

        String token = header.substring(BEARER_PREFIX_LENGTH);
        try {
            Long userId = tokenService.extractUserId(token);
            if (userId == null) {
                throw new CustomException("유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED);
            }

            request.setAttribute("userId", userId);
        } catch (Exception e) {
            throw new CustomException("유효하지 않은 토큰입니다.",HttpStatus.UNAUTHORIZED);
        }
        return true;
    }
}
