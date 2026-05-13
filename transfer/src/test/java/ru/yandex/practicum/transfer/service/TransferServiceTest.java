package ru.yandex.practicum.transfer.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.transfer.client.AccountClient;
import ru.yandex.practicum.transfer.client.NotificationClient;
import ru.yandex.practicum.transfer.dto.NotificationDto;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.mapper.TransferMapper;
import ru.yandex.practicum.transfer.model.Transfer;
import ru.yandex.practicum.transfer.repository.TransferRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульное тестирование перевода денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TransferServiceTest {
    private static final String ACCOUNT_FROM = "ivanov";
    private static final String ACCOUNT_TO = "petrov";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    @Mock
    private NotificationClient notificationClient;
    @Mock
    private AccountClient accountClient;
    @Mock
    private TransferRepository repository;
    @Mock
    private TransferMapper mapper;

    @InjectMocks
    private TransferService transferService;

    @Test
    @DisplayName("Перевод")
    void testSave_Success() {
        // Подготавливаем тестовые данные
        TransferRequestDto dto = TransferRequestDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Transfer transfer = Transfer.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Transfer saved = Transfer.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        TransferResponseDto mockResponse = TransferResponseDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        // Мок-действия
        doNothing().when(notificationClient).send(any(NotificationDto.class));
        doNothing().when(accountClient).transfer(dto);
        when(mapper.toEntity(dto)).thenReturn(transfer);
        when(repository.save(transfer)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(mockResponse);

        // Реальное действие
        TransferResponseDto response = transferService.save(dto);

        // Проверки
        assertEquals(ACCOUNT_FROM, response.getAccountFrom(), String.format("Аккаунт источника должен быть: %s", ACCOUNT_FROM));
        assertEquals(ACCOUNT_TO, response.getAccountTo(), String.format("Аккаунт получателя должен быть: %s", ACCOUNT_TO));
        assertEquals(AMOUNT, response.getAmount(), String.format("Сумма перевода должна быть: %d", AMOUNT));
        assertEquals(STATUS, response.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }

    @Test
    @DisplayName("Перевод (не достаточно средств)")
    void testSave_Error() {
        // Подготавливаем тестовые данные
        TransferRequestDto dto = TransferRequestDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Transfer transfer = Transfer.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Transfer saved = Transfer.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        // Мок-действия
        doNothing().when(notificationClient).send(any(NotificationDto.class));
        doThrow(new IllegalArgumentException("Error")).when(accountClient).transfer(dto);
        when(mapper.toEntity(dto)).thenReturn(transfer);
        when(repository.save(transfer)).thenReturn(saved);

        // Проверки
        assertThrows(
                IllegalArgumentException.class,
                () -> {TransferResponseDto response = transferService.save(dto);},
                "Должна быть ошибка"
        );
    }
}
