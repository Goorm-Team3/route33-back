package io.goorm.route33.model.dto;

import io.goorm.route33.exception.CustomException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequestDto {
    private String username;
    private String loginId;
    private String password;
    private String passwordConfirm;

    public void validate() {
        if (!StringUtils.hasText(username)) {
            throw new CustomException("이름은 필수 입력입니다.", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(loginId)) {
            throw new CustomException("로그인 아이디는 필수 입력입니다.", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(password)) {
            throw new CustomException("로그인 비밀번호는 필수 입력입니다.", HttpStatus.BAD_REQUEST);
        }

        if (!StringUtils.hasText(passwordConfirm)) {
            throw new CustomException("비밀번호 재확인은 필수 입력입니다.", HttpStatus.BAD_REQUEST);
        }
    }
}
