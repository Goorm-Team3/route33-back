package io.goorm.route33.service;

import io.goorm.route33.model.Account;
import io.goorm.route33.model.User;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    @DisplayName("사용자 생성 테스트")
    @Transactional
    void createUserTest() {
        UserRegisterRequestDto requestDto =
                new UserRegisterRequestDto("홍길동", "testuser1", "1234", "1234");

        User createdUser = userService.createUser(requestDto);

        Optional<User> foundUser = userRepository.findById(createdUser.getUserId());
        assertThat(foundUser).isPresent();
    }

    @Test
    @DisplayName("사용자 및 계좌 생성 테스트")
    @Transactional
    void createUserAndAccountTest() {
        UserRegisterRequestDto requestDto =
                new UserRegisterRequestDto("홍길동", "testuser1", "1234", "1234");

        userService.createUserAndAccount(requestDto);

        Optional<User> foundUser = userRepository.findByLoginId("testuser1");
        assertThat(foundUser).isPresent();
        Optional<Account> foundAccount = accountRepository.findByUserId(foundUser.get().getUserId());
        assertThat(foundAccount).isPresent();
    }
}