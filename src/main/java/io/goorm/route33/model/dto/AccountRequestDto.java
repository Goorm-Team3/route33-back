package io.goorm.route33.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AccountRequestDto {
    private int amount;
    private String accountNumber;
}
