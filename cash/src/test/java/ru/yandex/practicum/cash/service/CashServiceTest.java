package ru.yandex.practicum.cash.service;

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
import ru.yandex.practicum.cash.client.AccountClient;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.dto.NotificationDto;
import ru.yandex.practicum.cash.mapper.CashMapper;
import ru.yandex.practicum.cash.model.Cash;
import ru.yandex.practicum.cash.repository.CashRepository;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульное тестирование снятия / пополнения денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CashServiceTest {
    private static final String ACCOUNT = "ivanov";
    private static final String ACTION_POST = "POST";
    private static final String ACTION_GET = "GET";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    @Mock
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;
    @Mock
    private AccountClient accountClient;
    @Mock
    private CashRepository repository;
    @Mock
    private CashMapper mapper;
    @Mock
    private Tracer tracer;

    @InjectMocks
    private CashService cashService;

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
    @DisplayName("Пополнение")
    void testSave_POST() {
        // Подготавливаем тестовые данные
        CashRequestDto dto = CashRequestDto.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash cash = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash saved = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        CashResponseDto mockResponse = CashResponseDto.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        doNothing().when(accountClient).changeSum(dto);
        when(mapper.toEntity(dto)).thenReturn(cash);
        when(repository.save(cash)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(mockResponse);

        // Реальное действие
        CashResponseDto response = cashService.save(dto);

        // Проверки
        assertEquals(ACCOUNT, response.getAccount(), String.format("Аккаунт должен быть: %s", ACCOUNT));
        assertEquals(ACTION_POST, response.getAction(), String.format("Действие должно быть: %s", ACTION_POST));
        assertEquals(AMOUNT, response.getAmount(), String.format("Сумма пополнения должна быть: %d", AMOUNT));
        assertEquals(STATUS, response.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }

    @Test
    @DisplayName("Снятие")
    void testSave_GET() {
        // Подготавливаем тестовые данные
        CashRequestDto dto = CashRequestDto.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash cash = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash saved = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        CashResponseDto mockResponse = CashResponseDto.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        doNothing().when(accountClient).changeSum(dto);
        when(mapper.toEntity(dto)).thenReturn(cash);
        when(repository.save(cash)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(mockResponse);

        // Реальное действие
        CashResponseDto response = cashService.save(dto);

        // Проверки
        assertEquals(ACCOUNT, response.getAccount(), String.format("Аккаунт должен быть: %s", ACCOUNT));
        assertEquals(ACTION_GET, response.getAction(), String.format("Действие должно быть: %s", ACTION_GET));
        assertEquals(AMOUNT, response.getAmount(), String.format("Сумма снятия должна быть: %d", AMOUNT));
        assertEquals(STATUS, response.getStatus(), String.format("Статус должен быть: %s", STATUS));
    }

    @Test
    @DisplayName("Снятие (не достаточно средств)")
    void testSave_GET_ERROR() {
        // Подготавливаем тестовые данные
        CashRequestDto dto = CashRequestDto.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash cash = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        Cash saved = Cash.builder()
                .account(ACCOUNT)
                .action(ACTION_GET)
                .amount(AMOUNT)
                .status(STATUS)
                .build();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        doThrow(new IllegalArgumentException("Error")).when(accountClient).changeSum(dto);
        when(mapper.toEntity(dto)).thenReturn(cash);
        when(repository.save(cash)).thenReturn(saved);

        // Проверки
        assertThrows(
                IllegalArgumentException.class,
                () -> {CashResponseDto response = cashService.save(dto);},
                "Должна быть ошибка"
        );
    }
}
