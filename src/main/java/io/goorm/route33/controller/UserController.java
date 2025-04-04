package io.goorm.route33.controller;

import io.goorm.route33.auth.Auth;
import io.goorm.route33.auth.TokenPair;
import io.goorm.route33.auth.TokenService;
import io.goorm.route33.exception.CustomException;
import io.goorm.route33.model.User;
import io.goorm.route33.model.dto.*;
import io.goorm.route33.service.UserRepository;
import io.goorm.route33.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 유저 관련 컨트롤러
 */
@Slf4j
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenService tokenService;
    private final UserRepository userRepository;

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


//    @GetMapping("/me")
//    public ResponseEntity<?> myPage(@Auth Long userId) {
//        return ResponseEntity.ok(userId);
//    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody TokenRefreshRequestDto requestDto){

        UserLogoutResponseDto responseDto = userService.logout(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponseDto> refreshToken(@RequestBody TokenRefreshRequestDto requestDto){
        TokenPair tokenPair = tokenService.refreshTokenPair(requestDto.refreshToken());
        return ResponseEntity.ok(TokenRefreshResponseDto.from(tokenPair));
    }
}
