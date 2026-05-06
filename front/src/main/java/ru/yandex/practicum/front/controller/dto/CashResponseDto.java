package ru.yandex.practicum.front.controller.dto;

public record CashResponseDto(String account, String action, int amount, String status, String error) {
}
