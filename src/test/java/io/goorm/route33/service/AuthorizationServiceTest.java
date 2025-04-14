package io.goorm.route33.service;

import io.goorm.route33.auth.TokenService;
import io.goorm.route33.model.User;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import io.goorm.route33.auth.TokenPair;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AuthorizationServiceTest {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    private static final String LOGIN_ID = "testuser1";

    @BeforeEach
    void init() {
        // 테스트용 User 저장
        UserRegisterRequestDto requestDto =
                new UserRegisterRequestDto("홍길동", "testuser1", "1234", "1234");
        userService.createUserAndAccount(requestDto);

    }

    @Test
    @DisplayName("Access Token 생성 성공")
    void createAccessToken() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        String token = tokenService.createAccessToken(userId);

        assertThat(token).isNotNull();
        assertThat(token).startsWith("ey"); // JWT는 대체로 ey...로 시작
    }

    @Test
    @DisplayName("Refresh Token 생성 성공")
    void createRefreshToken() {
        String token = tokenService.createRefreshToken();

        assertThat(token).isNotNull();
    }

    @Test
    @DisplayName("TokenPair 생성 및 사용자 RefreshToken 저장 확인")
    void createTokenPair() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();

        TokenPair pair = tokenService.createTokenPair(userId);

        assertThat(pair.accessToken()).isNotNull();
        assertThat(pair.refreshToken()).isNotNull();

        User user = userRepository.getByUserId(userId);
        assertThat(user.getRefreshToken()).isEqualTo(pair.refreshToken());
    }

    @Test
    @DisplayName("RefreshToken 으로 새로운 TokenPair 발급")
    void refreshTokenPair() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();

        TokenPair pair = tokenService.createTokenPair(userId);

        TokenPair refreshed = tokenService.refreshTokenPair(pair.refreshToken());

        assertThat(refreshed.accessToken()).isNotNull();
        assertThat(refreshed.refreshToken()).isNotNull();
    }

    @Test
    @DisplayName("AccessToken 으로 사용자 ID 추출")
    void extractUserId() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();

        String token = tokenService.createAccessToken(userId);

        Long extractedId = tokenService.extractUserId(token);

        assertThat(extractedId).isEqualTo(userId);
    }


}
