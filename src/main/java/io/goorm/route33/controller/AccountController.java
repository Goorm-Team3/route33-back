package io.goorm.route33.controller;

import io.goorm.route33.auth.Auth;
import io.goorm.route33.auth.TokenService;
import io.goorm.route33.model.User;
import io.goorm.route33.model.dto.AccountInfoResponseDto;
import io.goorm.route33.model.dto.AccountRequestDto;
import io.goorm.route33.model.dto.CustomResponseDto;
import io.goorm.route33.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Member;

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
     * 계좌 정보 조회를 요청한다.
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<?> accountInfo(@Auth Long userId) {
        AccountInfoResponseDto responseDto = accountService.getAccountInfo(userId);

        return new ResponseEntity<>(new CustomResponseDto<>("조회 성공", responseDto), HttpStatus.OK);
    }

    /**
     * 계좌 입금을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@Auth Long userId, @RequestBody AccountRequestDto requestDto) {
        int balance = accountService.deposit(userId, requestDto.getAmount());

        return new ResponseEntity<>(new CustomResponseDto<>("입금 성공", balance), HttpStatus.OK);
    }

    /**
     * 계좌 출금을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdrawal(@Auth Long userId, @RequestBody AccountRequestDto requestDto) {
        int balance = accountService.withdrawal(userId, requestDto.getAmount());

        return new ResponseEntity<>(new CustomResponseDto<>("출금 성공", balance), HttpStatus.OK);
    }

    /**
     * 계좌 송금을 요청한다.
     *
     * @param requestDto
     * @return
     */
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Auth Long userId, @RequestBody AccountRequestDto requestDto) {
        accountService.transfer(userId, requestDto.getAccountNumber(), requestDto.getAmount());
        return new ResponseEntity<>(new CustomResponseDto<>("송금 성공", null), HttpStatus.OK);

    }
}
