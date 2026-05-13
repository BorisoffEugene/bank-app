package ru.yandex.practicum.transfer.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.model.Transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Модульное тестирование маппинга перевода денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TransferMapperTest {
    private static final String ACCOUNT_FROM = "ivanov";
    private static final String ACCOUNT_TO = "petrov";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    private final TransferMapper mapper = new TransferMapperImpl();

    @Test
    @DisplayName("TransferRequestDto -> Transfer")
    void testToEntity() {
        TransferRequestDto dto = TransferRequestDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        Transfer transfer = mapper.toEntity(dto);

        assertEquals(ACCOUNT_FROM, transfer.getAccountFrom(), String.format("Аккаунт источника должен быть: %s", ACCOUNT_FROM));
        assertEquals(ACCOUNT_TO, transfer.getAccountTo(), String.format("Аккаунт получателя должен быть: %s", ACCOUNT_TO));
        assertEquals(AMOUNT, transfer.getAmount(), String.format("Сумма перевода должна быть: %d", AMOUNT));
        assertEquals(STATUS, transfer.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }

    @Test
    @DisplayName("Transfer -> TransferResponseDto")
    void testToDto() {
        Transfer transfer = Transfer.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        TransferResponseDto dto = mapper.toDto(transfer);

        assertEquals(ACCOUNT_FROM, dto.getAccountFrom(), String.format("Аккаунт источника должен быть: %s", ACCOUNT_FROM));
        assertEquals(ACCOUNT_TO, dto.getAccountTo(), String.format("Аккаунт получателя должен быть: %s", ACCOUNT_TO));
        assertEquals(AMOUNT, dto.getAmount(), String.format("Сумма перевода должна быть: %d", AMOUNT));
        assertEquals(STATUS, dto.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }
}
