package ru.yandex.practicum.cash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CashResponseDto {
    private String account;
    private String action;
    private int amount;
    private String status;
    private String error;
}
