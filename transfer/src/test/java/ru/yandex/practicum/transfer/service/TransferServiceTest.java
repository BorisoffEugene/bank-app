package ru.yandex.practicum.transfer.service;

import io.micrometer.tracing.CurrentTraceContext;
import io.micrometer.tracing.TraceContext;
import io.micrometer.tracing.Tracer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.yandex.practicum.transfer.client.AccountClient;
import ru.yandex.practicum.transfer.client.NotificationClient;
import ru.yandex.practicum.transfer.dto.NotificationDto;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.mapper.TransferMapper;
import ru.yandex.practicum.transfer.model.Transfer;
import ru.yandex.practicum.transfer.repository.TransferRepository;

import java.util.concurrent.CompletableFuture;

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
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;
    @Mock
    private AccountClient accountClient;
    @Mock
    private TransferRepository repository;
    @Mock
    private TransferMapper mapper;
    @Mock
    private Tracer tracer;

    @InjectMocks
    private TransferService transferService;

    @BeforeEach
    void setUpTracer() {
        CurrentTraceContext currentTraceContext = Mockito.mock(CurrentTraceContext.class);
        TraceContext traceContext = Mockito.mock(TraceContext.class);

        when(tracer.currentTraceContext()).thenReturn(currentTraceContext);
        when(currentTraceContext.context()).thenReturn(traceContext);

        when(traceContext.traceId()).thenReturn("mock-trace-id");
        when(traceContext.spanId()).thenReturn("mock-span-id");
    }

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
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
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
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
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
