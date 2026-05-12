package ru.yandex.practicum.notifications.contract;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.notifications.dto.NotificationDto;
import ru.yandex.practicum.notifications.service.NotificationLogService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseNotificationContractTest {
    @Autowired
    protected MockMvc mockMvc;

    @MockitoBean
    protected NotificationLogService notificationLogService;

    @BeforeEach
    void setup() {
        RestAssuredMockMvc.mockMvc(mockMvc);

        RestAssuredMockMvc.authentication = RestAssuredMockMvc.with(
                jwt().authorities(
                        List.of(
                                new SimpleGrantedAuthority("ROLE_SERVICE"),
                                new SimpleGrantedAuthority("notification.write")
                        )
                )
        );

        doNothing().when(notificationLogService).send(any(NotificationDto.class));
    }
}
