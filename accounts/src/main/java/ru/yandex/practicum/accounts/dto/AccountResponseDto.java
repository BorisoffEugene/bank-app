package ru.yandex.practicum.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class AccountResponseDto {
    private String name;
    private LocalDate birthdate;
    private int sum;
}
