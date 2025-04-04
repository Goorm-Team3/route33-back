package io.goorm.route33.auth;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
