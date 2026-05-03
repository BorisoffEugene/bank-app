package ru.yandex.practicum.accounts.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.yandex.practicum.accounts.annotation.Adult;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class AccountRequestDto {
    @NotBlank(message = "Логин должен быть")
    private String login;

    @NotBlank(message = "Поле 'Фамилия' должно быть заполнено")
    private String name;

    @Adult()
    private LocalDate birthdate;
}
