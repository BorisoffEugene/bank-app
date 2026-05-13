package ru.yandex.practicum.transfer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.service.TransferService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransferController.class)
@DisplayName("Интеграционное (WEB) тестирование перевода средств")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TransferControllerTest {
    private static final String ACCOUNT_FROM = "ivanov";
    private static final String ACCOUNT_TO = "petrov";
    private static final int AMOUNT = 100;
    private static final String STATUS = "OK";

    private TransferResponseDto response;
    private TransferRequestDto request;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransferService transferService;

    @BeforeEach
    void setUp() {
        response = TransferResponseDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
        request = TransferRequestDto.builder()
                .accountFrom(ACCOUNT_FROM)
                .accountTo(ACCOUNT_TO)
                .amount(AMOUNT)
                .status(STATUS)
                .build();
    }

    @Test
    @DisplayName("Перевод")
    void testSave() throws Exception {
        when(transferService.save(any(TransferRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", ACCOUNT_FROM)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountFrom").value(ACCOUNT_FROM))
                .andExpect(jsonPath("$.accountTo").value(ACCOUNT_TO))
                .andExpect(jsonPath("$.amount").value(AMOUNT))
                .andExpect(jsonPath("$.status").value(STATUS));
    }

    @Test
    @DisplayName("Перевод (нет авторизации)")
    void testSave_Return403() throws Exception {
        when(transferService.save(any(TransferRequestDto.class))).thenReturn(response);

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Перевод (недостаточно средств)")
    void testSave_Error() throws Exception {
        doThrow(new IllegalArgumentException("Error")).when(transferService).save(any(TransferRequestDto.class));

        mockMvc.perform(post("/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().jwt(builder -> builder.claim("preferred_username", ACCOUNT_FROM)))
                )
                .andExpect(status().isNotFound());
    }
}
