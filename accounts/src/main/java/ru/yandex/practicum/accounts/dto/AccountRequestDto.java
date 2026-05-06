package ru.yandex.practicum.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import ru.yandex.practicum.accounts.annotation.Adult;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AccountRequestDto {
    private String login;

    @NotBlank(message = "Поле 'Фамилия' должно быть заполнено")
    private String name;

    @Adult()
    private LocalDate birthdate;

    @PositiveOrZero
    private int sum;
}
