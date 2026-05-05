package ru.yandex.practicum.transfer.dto;

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
public class TransferRequestDto {
    private String accountFrom;

    @NotBlank(message = "Получатель не должен быть пустым")
    private String accountTo;

    @Positive(message = "Сумма должна быть больше нуля")
    private int amount;
}
