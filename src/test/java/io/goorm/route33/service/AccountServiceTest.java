package io.goorm.route33.service;

import io.goorm.route33.model.dto.UserRegisterRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private static final String LOGIN_ID = "user1";

    @BeforeEach
    void init() {
        UserRegisterRequestDto requestDto =
                new UserRegisterRequestDto("테스트", LOGIN_ID, "1234", "1234");

        userService.createUserAndAccount(requestDto);
    }

    @Test
    @DisplayName("계좌 입금 테스트")
    @Transactional
    void depositTest() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        int amount = 50000;

        int balanceAfterDeposit = accountService.deposit(userId, amount);

        Assertions.assertThat(balanceAfterDeposit).isEqualTo(amount);
    }
}