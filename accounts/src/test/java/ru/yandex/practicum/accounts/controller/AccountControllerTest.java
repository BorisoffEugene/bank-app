package ru.yandex.practicum.accounts.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.dto.CashRequestDto;
import ru.yandex.practicum.accounts.dto.TransferRequestDto;
import ru.yandex.practicum.accounts.service.AccountService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@DisplayName("Интеграционное (WEB) тестирование аккаунта")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AccountControllerTest {
    private static final String LOGIN = "ivanov";
    private static final String WRONG_LOGIN = "wrong_login";
    private static final String NAME = "Иванов Иван";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 3, 12);

    private AccountResponseDto response;
    private AccountRequestDto request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        response = AccountResponseDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();
        request = AccountRequestDto.builder()
                .login(LOGIN)
                .name(NAME)
                .birthdate(BIRTHDATE)
                .build();
    }

    @Test
    @DisplayName("Получение данных аккаунта (данные есть)")
    void testFindByLogin_Success() throws Exception {
        when(accountService.findByLogin(LOGIN)).thenReturn(response);

        mockMvc.perform(get("/account")
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(LOGIN))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.birthdate").value(BIRTHDATE.toString()));
    }

    @Test
    @DisplayName("Получение данных аккаунта (данных нет)")
    void testFindByLogin_NotFound() throws Exception {
        when(accountService.findByLogin(WRONG_LOGIN)).thenThrow(new IllegalArgumentException("Error"));

        mockMvc.perform(get("/account")
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", WRONG_LOGIN)))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Получение данных аккаунта (нет авторизации)")
    void testFindByLogin_Return3xx() throws Exception {
        when(accountService.findByLogin(LOGIN)).thenReturn(response);

        mockMvc.perform(get("/account"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Получение остальных аккаунтов (данные есть)")
    void testFindOtherAccounts_Success() throws Exception {
        when(accountService.findOtherAccounts(LOGIN)).thenReturn(List.of(response));

        mockMvc.perform(get("/account/other-accounts")
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value(LOGIN))
                .andExpect(jsonPath("$[0].name").value(NAME))
                .andExpect(jsonPath("$[0].birthdate").value(BIRTHDATE.toString()));
    }

    @Test
    @DisplayName("Получение остальных аккаунтов (данных нет)")
    void testFindOtherAccounts_NotFound() throws Exception {
        when(accountService.findOtherAccounts(LOGIN)).thenReturn(List.of());

        mockMvc.perform(get("/account/other-accounts")
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Сохранение данных аккаунта")
    void testSave() throws Exception {
        when(accountService.save(any(AccountRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(LOGIN))
                .andExpect(jsonPath("$.name").value(NAME))
                .andExpect(jsonPath("$.birthdate").value(BIRTHDATE.toString()));
    }

    @Test
    @DisplayName("Изменение суммы")
    void testChangeSum_Success() throws Exception {
        CashRequestDto cashRequest = CashRequestDto.builder()
                .account(LOGIN)
                .action("PUT")
                .amount(100)
                .build();

        doNothing().when(accountService).changeSum(any(CashRequestDto.class));

        mockMvc.perform(post("/account/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cashRequest))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Изменение суммы (недостаточно средств)")
    void testChangeSum_Error() throws Exception {
        CashRequestDto cashRequest = CashRequestDto.builder()
                .account(LOGIN)
                .action("GET")
                .amount(100)
                .build();

        doThrow(new IllegalArgumentException("Error")).when(accountService).changeSum(any(CashRequestDto.class));

        mockMvc.perform(post("/account/change")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cashRequest))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Перевод")
    void testTransfer_Success() throws Exception {
        TransferRequestDto transferRequest = TransferRequestDto.builder()
                .accountFrom(LOGIN)
                .accountTo("petrov")
                .amount(100)
                .build();

        doNothing().when(accountService).transfer(any(TransferRequestDto.class));

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Перевод (недостаточно средств)")
    void testTransfer_Error() throws Exception {
        TransferRequestDto transferRequest = TransferRequestDto.builder()
                .accountFrom(LOGIN)
                .accountTo("petrov")
                .amount(100)
                .build();

        doThrow(new IllegalArgumentException("Error")).when(accountService).transfer(any(TransferRequestDto.class));

        mockMvc.perform(post("/account/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transferRequest))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isNotFound());
    }
}
