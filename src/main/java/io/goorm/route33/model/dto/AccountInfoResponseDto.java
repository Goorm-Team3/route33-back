package io.goorm.route33.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccountInfoResponseDto {
    private String username;
    private String accountNumber;
    private int balance;
}
