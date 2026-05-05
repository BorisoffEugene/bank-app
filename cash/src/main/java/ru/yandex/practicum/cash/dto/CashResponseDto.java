package ru.yandex.practicum.cash.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class CashResponseDto {
    private String account;
    private CashAction action;
    private int amount;
    private ActionStatus status;
    private String error;
}
