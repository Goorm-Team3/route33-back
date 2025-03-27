package io.goorm.route33.controller;

import io.goorm.route33.model.dto.AccountRequestDto;
import io.goorm.route33.model.dto.CustomResponseDto;
import io.goorm.route33.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 계좌 관련 컨트롤러
 */
@Slf4j
@RequestMapping("/account")
@RestController
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    /**
     * 계좌 입금을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody AccountRequestDto requestDto) {
        Long userId = 1L; // TODO 로그인 방식 구현 후 수정한다.
        int balance = accountService.deposit(userId, requestDto.getAmount());

        return new ResponseEntity<>(new CustomResponseDto<>("입금 성공", balance), HttpStatus.OK);
    }

    /**
     * 계좌 출금을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdrawal(@RequestBody AccountRequestDto requestDto) {
        Long userId = 1L; // TODO 로그인 방식 구현 후 수정한다.
        int balance = accountService.withdrawal(userId, requestDto.getAmount());

        return new ResponseEntity<>(new CustomResponseDto<>("출금 성공", balance), HttpStatus.OK);
    }
}
