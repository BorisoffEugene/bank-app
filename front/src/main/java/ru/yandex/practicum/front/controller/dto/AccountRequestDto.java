package ru.yandex.practicum.front.controller.dto;

import java.time.LocalDate;

public record AccountRequestDto(String name, LocalDate birthdate) {
}
