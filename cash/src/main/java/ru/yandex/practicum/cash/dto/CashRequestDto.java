package ru.yandex.practicum.cash.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class CashRequestDto {
    private String account;

    @NotBlank(message = "Действие не должно быть пустым")
    private String action;

    @Positive(message = "Сумма должна быть больше нуля")
    private int amount;

    private String status;
}
