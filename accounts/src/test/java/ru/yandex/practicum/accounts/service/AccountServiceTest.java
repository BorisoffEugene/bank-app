package ru.yandex.practicum.accounts.service;

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
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.dto.NotificationDto;
import ru.yandex.practicum.accounts.mapper.AccountMapper;
import ru.yandex.practicum.accounts.model.Account;
import ru.yandex.practicum.accounts.repository.AccountRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Модульное тестирование аккаунта")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AccountServiceTest {
    private static final String LOGIN = "ivanov";
    private static final String WRONG_LOGIN = "wrong_login";
    private static final String NAME = "Иванов Иван";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 3, 12);

    @Mock
    private KafkaTemplate<String, NotificationDto> kafkaTemplate;
    @Mock
    private AccountRepository repository;
    @Mock
    private AccountMapper mapper;
    @Mock
    private Tracer tracer;

    @InjectMocks
    private AccountService accountService;

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
    @DisplayName("Получение остальных аккаунтов (данных нет)")
    void testFindOtherAccounts_NotFound() {
        // Подготавливаем тестовые данные
        List<AccountResponseDto> mockResponse = List.of();
        List<Account> mockAccounts = List.of();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(repository.findByLoginNot(LOGIN)).thenReturn(mockAccounts);
        for (int i = 0; i < mockAccounts.size(); i++)
            when(mapper.toDto(mockAccounts.get(i))).thenReturn(mockResponse.get(i));

        // Реальное действие
        List<AccountResponseDto> response = accountService.findOtherAccounts(LOGIN);

        // Проверки
        assertEquals(0, response.size(), String.format("Количество должно быть: %d", 0));
    }

    @Test
    @DisplayName("Получение остальных аккаунтов (данные есть)")
    void testFindOtherAccounts_Success() {
        // Подготавливаем тестовые данные
        List<AccountResponseDto> mockResponse = List.of(
                new AccountResponseDto("petrov", "Петров Петр", LocalDate.of(2000, 3, 12), 0),
                new AccountResponseDto("sidorov", "Сидоров Сидор", LocalDate.of(2001, 6, 15), 100)
        );
        List<Account> mockAccounts = List.of(
                new Account("petrov", "Петров Петр", LocalDate.of(2000, 3, 12), 0),
                new Account("sidorov", "Сидоров Сидор", LocalDate.of(2001, 6, 15), 100)
        );


        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(repository.findByLoginNot(LOGIN)).thenReturn(mockAccounts);
        for (int i = 0; i < mockAccounts.size(); i++)
            when(mapper.toDto(mockAccounts.get(i))).thenReturn(mockResponse.get(i));

        // Реальное действие
        List<AccountResponseDto> response = accountService.findOtherAccounts(LOGIN);

        // Проверки
        assertEquals(mockResponse.size(), response.size(), String.format("Количество должно быть: %d", mockResponse.size()));
        assertEquals(mockResponse.getFirst().getLogin(), response.getFirst().getLogin(), String.format("Логин должен быть: %s", mockResponse.getFirst().getLogin()));
        assertEquals(mockResponse.getFirst().getName(), response.getFirst().getName(), String.format("Имя должно быть: %s", mockResponse.getFirst().getName()));
        assertEquals(mockResponse.getFirst().getBirthdate(), response.getFirst().getBirthdate(), String.format("Дата рождения должна быть: %tF", mockResponse.getFirst().getBirthdate()));
    }

    @Test
    @DisplayName("Получение данных аккаунта (данные есть)")
    void testFindByLogin_Success() {
        // Подготавливаем тестовые данные
        Optional<Account> optionalAccount = Optional.of(Account.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build());
        AccountResponseDto mockResponse = AccountResponseDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(repository.findByLogin(LOGIN)).thenReturn(optionalAccount);
        when(mapper.toDto(optionalAccount.get())).thenReturn(mockResponse);

        // Реальное действие
        AccountResponseDto response = accountService.findByLogin(LOGIN);

        // Проверки
        assertEquals(LOGIN, response.getLogin(), String.format("Логин должен быть: %s", LOGIN));
        assertEquals(NAME, response.getName(), String.format("Имя должно быть: %s", NAME));
        assertEquals(BIRTHDATE, response.getBirthdate(), String.format("Дата рождения должна быть: %tF", BIRTHDATE));
    }

    @Test
    @DisplayName("Получение данных аккаунта (данных нет)")
    void testFindByLogin_NotFound() {
        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(repository.findByLogin(WRONG_LOGIN)).thenReturn(Optional.empty());

        // Проверки
        assertThrows(
                IllegalArgumentException.class,
                () -> {AccountResponseDto response = accountService.findByLogin(WRONG_LOGIN);},
                "Должна быть ошибка"
        );
    }

    @Test
    @DisplayName("Сохранение данных аккаунта")
    void testSave() {
        // Подготавливаем тестовые данные
        AccountRequestDto dto = AccountRequestDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();
        Account account = Account.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();
        Account saved = Account.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();
        AccountResponseDto mockResponse = AccountResponseDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();

        // Мок-действия
        when(kafkaTemplate.send(eq("notification"), any(NotificationDto.class))).thenReturn(CompletableFuture.completedFuture(null));
        when(mapper.toEntity(dto)).thenReturn(account);
        when(repository.save(account)).thenReturn(saved);
        when(mapper.toDto(saved)).thenReturn(mockResponse);

        // Реальное действие
        AccountResponseDto response = accountService.save(dto);

        // Проверки
        assertEquals(LOGIN, response.getLogin(), String.format("Логин должен быть: %s", LOGIN));
        assertEquals(NAME, response.getName(), String.format("Имя должно быть: %s", NAME));
        assertEquals(BIRTHDATE, response.getBirthdate(), String.format("Дата рождения должна быть: %tF", BIRTHDATE));
    }
}
