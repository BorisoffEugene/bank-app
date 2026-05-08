package ru.yandex.practicum.cash.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.model.Cash;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Модульное тестирование маппинга снятия / пополнения денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CashMapperTest {
    private static final String ACCOUNT = "ivanov";
    private static final String ACTION_POST = "POST";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    private final CashMapper mapper = new CashMapperImpl();

    @Test
    @DisplayName("CashRequestDto -> Cash")
    void testToEntity() {
        CashRequestDto dto = CashRequestDto.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        Cash cash = mapper.toEntity(dto);

        assertEquals(ACCOUNT, cash.getAccount(), String.format("Аккаунт должен быть: %s", ACCOUNT));
        assertEquals(ACTION_POST, cash.getAction(), String.format("Действие должно быть: %s", ACTION_POST));
        assertEquals(AMOUNT, cash.getAmount(), String.format("Сумма пополнения должна быть: %d", AMOUNT));
        assertEquals(STATUS, cash.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }

    @Test
    @DisplayName("Cash -> CashResponseDto")
    void testToDto() {
        Cash cash = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        CashResponseDto dto = mapper.toDto(cash);

        assertEquals(ACCOUNT, dto.getAccount(), String.format("Аккаунт должен быть: %s", ACCOUNT));
        assertEquals(ACTION_POST, dto.getAction(), String.format("Действие должно быть: %s", ACTION_POST));
        assertEquals(AMOUNT, dto.getAmount(), String.format("Сумма пополнения должна быть: %d", AMOUNT));
        assertEquals(STATUS, dto.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }
}
