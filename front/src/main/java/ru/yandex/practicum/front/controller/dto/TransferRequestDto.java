package ru.yandex.practicum.front.controller.dto;

public record TransferRequestDto(String accountTo, int amount, String status) {
}
