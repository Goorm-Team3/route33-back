package io.goorm.route33.service;

import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.User;
import io.goorm.route33.model.code.OnOffStatus;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 생성하고 계좌를 등록한다.
     *
     * @param requestDto
     */
    @Transactional
    public void createUserAndAccount(UserRegisterRequestDto requestDto) {
        User user = createUser(requestDto);
        accountService.createAccount(user.getUserId());
    }

    public User createUser(UserRegisterRequestDto requestDto) {
        validateRegisterInfo(requestDto);

        String encryptedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
                .username(requestDto.getUsername())
                .loginId(requestDto.getLoginId())
                .password(encryptedPassword)
                .createTime(LocalDateTime.now())
                .activeYn(OnOffStatus.ON)
                .build();

        return userRepository.save(user);
    }

    private void validateRegisterInfo(UserRegisterRequestDto requestDto) {
        requestDto.validate();

        if (isExistLoginId(requestDto.getLoginId())) {
            throw new CustomException("이미 존재하는 아이디입니다.", HttpStatus.BAD_REQUEST);
        }

        if (!isValidPassword(requestDto.getPassword(), requestDto.getPasswordConfirm())) {
            throw new CustomException("입력하신 비밀번호와 재확인 비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isExistLoginId(String loginId) {
        return userRepository.findByLoginId(loginId).isPresent();
    }

    private boolean isValidPassword(String password, String passwordConfirm) {
        return password.equals(passwordConfirm);
    }
}
