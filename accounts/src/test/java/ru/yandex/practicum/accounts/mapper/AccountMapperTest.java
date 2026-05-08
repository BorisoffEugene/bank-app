package ru.yandex.practicum.accounts.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.model.Account;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Модульное тестирование маппинга аккаунта")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AccountMapperTest {
    private static final String LOGIN = "ivanov";
    private static final String NAME = "Иванов Иван";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 3, 12);

    private final AccountMapper mapper = new AccountMapperImpl();

    @Test
    @DisplayName("AccountRequestDto -> Account")
    void testToEntity() {
        AccountRequestDto dto = AccountRequestDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();

        Account account = mapper.toEntity(dto);

        assertEquals(LOGIN, account.getLogin(), String.format("Логин должен быть: %s", LOGIN));
        assertEquals(NAME, account.getName(), String.format("Имя должно быть: %s", NAME));
        assertEquals(BIRTHDATE, account.getBirthdate(), String.format("Дата рождения должна быть: %tF", BIRTHDATE));
    }

    @Test
    @DisplayName("Account -> AccountResponseDto")
    void testToDto() {
        Account account = Account.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();

        AccountResponseDto dto = mapper.toDto(account);

        assertEquals(LOGIN, dto.getLogin(), String.format("Логин должен быть: %s", LOGIN));
        assertEquals(NAME, dto.getName(), String.format("Имя должно быть: %s", NAME));
        assertEquals(BIRTHDATE, dto.getBirthdate(), String.format("Дата рождения должна быть: %tF", BIRTHDATE));
    }
}
