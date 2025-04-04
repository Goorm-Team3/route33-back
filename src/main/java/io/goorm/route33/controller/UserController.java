package io.goorm.route33.controller;

import io.goorm.route33.auth.TokenPair;
import io.goorm.route33.model.dto.CustomResponseDto;
import io.goorm.route33.model.dto.UserLoginRequestDto;
import io.goorm.route33.model.dto.UserRegisterRequestDto;
import io.goorm.route33.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 유저 관련 컨트롤러
 */
@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * 회원 가입을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody UserRegisterRequestDto requestDto) {
        userService.createUserAndAccount(requestDto);

        return new ResponseEntity<>(new CustomResponseDto<>("회원가입 성공", null), HttpStatus.OK);
    }
    @PostMapping("/login")

    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto requestDto) {
        TokenPair tokenPair = userService.login(requestDto);
        return ResponseEntity.ok(tokenPair);
    }
}
