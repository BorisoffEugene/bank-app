package ru.yandex.practicum.transfer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TransferResponseDto {
    private String accountFrom;
    private String accountTo;
    private int amount;
    private ActionStatus status;
    private String error;
}
