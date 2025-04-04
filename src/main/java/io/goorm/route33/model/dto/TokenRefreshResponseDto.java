package io.goorm.route33.model.dto;

import io.goorm.route33.auth.TokenPair;

public record TokenRefreshResponseDto(
        String accessToken,
        String refreshToken
) {
    public static TokenRefreshResponseDto from(TokenPair tokenPair){
        return new TokenRefreshResponseDto(tokenPair.accessToken(), tokenPair.refreshToken());
    }
}
