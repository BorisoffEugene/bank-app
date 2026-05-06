package ru.yandex.practicum.front.controller.dto;

public record TransferResponseDto(String accountFrom, String accountTo, int amount, String status, String error) {
}
