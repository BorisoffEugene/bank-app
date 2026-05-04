package ru.yandex.practicum.front.controller.dto;

import java.time.LocalDate;

public record AccountResponseDto(String login, String name, LocalDate birthdate, int sum) {
}
