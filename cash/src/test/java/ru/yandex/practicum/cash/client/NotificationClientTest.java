package ru.yandex.practicum.cash.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import ru.yandex.practicum.cash.dto.NotificationDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "stubrunner.ids-to-service-ids.notifications=notification-service"
)
@AutoConfigureStubRunner(
        ids = {"ru.yandex.practicum:notifications:+:stubs:8085"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@DisplayName("Контрактное тестирование оповещений")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class NotificationClientTest {
    @Autowired
    private NotificationClient notificationClient;

    @Test
    @DisplayName("Оповещение")
    void testSend() {
        assertDoesNotThrow(() -> notificationClient.send(new NotificationDto("message")));
    }
}