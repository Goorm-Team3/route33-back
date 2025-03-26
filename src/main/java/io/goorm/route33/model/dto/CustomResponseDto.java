package io.goorm.route33.model.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CustomResponseDto<T> {
    private String message;
    private T data;
}
