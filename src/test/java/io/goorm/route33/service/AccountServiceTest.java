package io.goorm.route33.service;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.Account;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@SpringBootTest
class AccountServiceTest {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
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
    void depositTest() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        int amount = 50000;

        int balanceAfterDeposit = accountService.deposit(userId, amount);

        assertThat(balanceAfterDeposit).isEqualTo(amount);
    }

    @Test
    @DisplayName("계좌 출금 테스트")
    void withdrawalTest() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        int initBalance = 50000;
        accountService.deposit(userId, initBalance);
        int amount = 30000;

        int balanceAfterWithdrawal = accountService.withdrawal(userId, amount);

        assertThat(balanceAfterWithdrawal).isEqualTo(initBalance - amount);
    }

    @Test
    @DisplayName("계좌 출금 테스트 - 잔액 부족 케이스")
    void withdrawalInsufficientBalanceTest() {
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        int amount = 30000;

        assertThatThrownBy(() -> accountService.withdrawal(userId, amount))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining("출금을 위한 잔액이 부족합니다");
    }

    @Test
    @DisplayName("송금 테스트")
    void transferTest() {
        // 송금 보내는 계좌 잔액 50000원 세팅
        Long userId = userRepository.findByLoginId(LOGIN_ID).get().getUserId();
        int initBalance = 50000;
        accountService.deposit(userId, initBalance);

        // 송금 받을 계좌 생성
        UserRegisterRequestDto requestDto =
                new UserRegisterRequestDto("테스트2", "user2", "1234", "1234");
        userService.createUserAndAccount(requestDto);
        Long userId2 = userRepository.findByLoginId("user2").get().getUserId();
        String targetAccountNumber = accountRepository.findByUserId(userId2).get().getAccountNumber();

        // WHEN
        int amount = 30000;
        accountService.transfer(userId, targetAccountNumber, amount);

        Account fromAccount = accountRepository.findByUserId(userId).get();
        Account toAccount = accountRepository.findByUserId(userId2).get();
        assertThat(fromAccount.getBalance()).isEqualTo(20000);
        assertThat(toAccount.getBalance()).isEqualTo(30000);
    }
}