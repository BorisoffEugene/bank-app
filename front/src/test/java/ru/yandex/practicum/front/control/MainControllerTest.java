package ru.yandex.practicum.front.control;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.front.client.AccountClient;
import ru.yandex.practicum.front.client.CashClient;
import ru.yandex.practicum.front.client.TransferClient;
import ru.yandex.practicum.front.controller.MainController;
import ru.yandex.practicum.front.controller.dto.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
@DisplayName("Интеграционное (WEB) тестирование фронта")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class MainControllerTest {
    private static final String LOGIN = "ivanov";
    private static final String NAME = "Иванов Иван";
    private static final LocalDate BIRTHDATE = LocalDate.of(2000, 3, 12);
    private static final int SUM = 100;

    private static final AccountResponseDto account = new AccountResponseDto(LOGIN, NAME, BIRTHDATE, SUM);
    private static final List<AccountResponseDto> accounts = List.of(new AccountResponseDto("petrov", "Петров Петр", BIRTHDATE, SUM));
    private static final CashResponseDto cash = new CashResponseDto(LOGIN, "POST", 200, "OK", "");
    private static final TransferResponseDto transfer = new TransferResponseDto(LOGIN, "petrov", 100, "OK", "");

    @MockitoBean
    private AccountClient accountClient;
    @MockitoBean
    private CashClient cashClient;
    @MockitoBean
    private TransferClient transferClient;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = LOGIN)
    @DisplayName("Начальная страница")
    void testIndex() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/account"))
                .andExpect(view().name("redirect:/account"));
    }

    @Test
    @DisplayName("Начальная страница (нет авторизации)")
    void testIndex_NoUser() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/keycloak"));
    }

    @Test
    @WithMockUser(username = LOGIN)
    @DisplayName("Получение данных")
    void testGetAccount() throws Exception {
        when(accountClient.findByLogin()).thenReturn(account);
        when(accountClient.findOtherAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("name", NAME))
                .andExpect(model().attribute("birthdate", BIRTHDATE.format(DateTimeFormatter.ISO_DATE)))
                .andExpect(model().attribute("sum", SUM));
    }

    @Test
    @WithMockUser(username = LOGIN)
    @DisplayName("Сохранение данных")
    void testEditAccount() throws Exception {
        when(accountClient.save(any(AccountRequestDto.class))).thenReturn(account);
        when(accountClient.findByLogin()).thenReturn(account);
        when(accountClient.findOtherAccounts()).thenReturn(accounts);

        mockMvc.perform(post("/account")
                        .param("name", NAME)
                        .param("birthdate", BIRTHDATE.format(DateTimeFormatter.ISO_DATE))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("name", NAME))
                .andExpect(model().attribute("birthdate", BIRTHDATE.format(DateTimeFormatter.ISO_DATE)))
                .andExpect(model().attribute("sum", SUM));
    }

    @Test
    @WithMockUser(username = LOGIN)
    @DisplayName("Изменение суммы")
    void testEditCash() throws Exception {
        when(cashClient.save(any(CashRequestDto.class))).thenReturn(cash);
        when(accountClient.findByLogin()).thenReturn(account);
        when(accountClient.findOtherAccounts()).thenReturn(accounts);

        mockMvc.perform(post("/cash")
                        .param("value", "200")
                        .param("action", CashAction.GET.name())
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("name", NAME))
                .andExpect(model().attribute("birthdate", BIRTHDATE.format(DateTimeFormatter.ISO_DATE)))
                .andExpect(model().attribute("sum", SUM));
    }

    @Test
    @WithMockUser(username = LOGIN)
    @DisplayName("Перевод")
    void testTransfer() throws Exception {
        when(transferClient.save(any(TransferRequestDto.class))).thenReturn(transfer);
        when(accountClient.findByLogin()).thenReturn(account);
        when(accountClient.findOtherAccounts()).thenReturn(accounts);

        mockMvc.perform(post("/transfer")
                        .param("value", "100")
                        .param("login", "petrov")
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", LOGIN)))
                )
                .andExpect(status().isOk())
                .andExpect(view().name("main"))
                .andExpect(model().attribute("name", NAME))
                .andExpect(model().attribute("birthdate", BIRTHDATE.format(DateTimeFormatter.ISO_DATE)))
                .andExpect(model().attribute("sum", SUM));
    }
}
