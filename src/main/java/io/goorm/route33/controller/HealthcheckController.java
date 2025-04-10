package io.goorm.route33.controller;

import io.goorm.route33.auth.Auth;
import io.goorm.route33.model.dto.AccountInfoResponseDto;
import io.goorm.route33.model.dto.CustomResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/health")
@RestController
@RequiredArgsConstructor
public class HealthcheckController {
    @GetMapping
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}
