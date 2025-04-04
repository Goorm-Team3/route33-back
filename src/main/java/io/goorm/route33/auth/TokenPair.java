package io.goorm.route33.auth;

public record TokenPair( // DTO
        String accessToken,
         String refreshToken
) {
}
