package ru.yandex.practicum.notifications.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.notifications.dto.NotificationDto;
import ru.yandex.practicum.notifications.service.NotificationLogService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificationController.class)
@DisplayName("Интеграционное (WEB) тестирование оповещений")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class NotificationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificationLogService notificationLogService;

    @Test
    @DisplayName("Оповещение")
    void testSend() throws Exception {
        doNothing().when(notificationLogService).send(any(NotificationDto.class));

        mockMvc.perform(post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NotificationDto("test message")))
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ROLE_SERVICE"),
                                new SimpleGrantedAuthority("notification.write")
                        ))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Оповещение (нет авторизации)")
    void testSend_Return403() throws Exception {
        doNothing().when(notificationLogService).send(any(NotificationDto.class));

        mockMvc.perform(post("/notification")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new NotificationDto("test message")))
                )
                .andExpect(status().isForbidden());
    }

}
