package ru.yandex.practicum.cash.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.service.CashService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashController.class)
@DisplayName("Интеграционное (WEB) тестирование снятия/перевода денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CashControllerTest {
    private static final String ACCOUNT = "ivanov";
    private static final String ACTION_POST = "POST";
    private static final String ACTION_GET = "GET";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    private CashResponseDto responsePost;
    private CashRequestDto request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CashService cashService;

    @BeforeEach
    void setUp() {
        responsePost = CashResponseDto.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        request = CashRequestDto.builder()
                .account(ACCOUNT)
                .action(ACTION_POST)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
    }

    @Test
    @DisplayName("Пополнение")
    void testSave_POST() throws Exception {
        when(cashService.save(any(CashRequestDto.class))).thenReturn(responsePost);

        mockMvc.perform(post("/cash")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", ACCOUNT)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account").value(ACCOUNT))
                .andExpect(jsonPath("$.action").value(ACTION_POST))
                .andExpect(jsonPath("$.amount").value(AMOUNT))
                .andExpect(jsonPath("$.status").value(STATUS));
    }

    @Test
    @DisplayName("Пополнение (нет авторизации)")
    void testSave_Return403() throws Exception {
        when(cashService.save(any(CashRequestDto.class))).thenReturn(responsePost);

        mockMvc.perform(post("/cash")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Снятие (недостаточно средств)")
    void testSave_Error() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(cashService).save(any(CashRequestDto.class));

        mockMvc.perform(post("/cash")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", ACCOUNT)))
                )
                .andExpect(status().isNotFound());
    }
}
