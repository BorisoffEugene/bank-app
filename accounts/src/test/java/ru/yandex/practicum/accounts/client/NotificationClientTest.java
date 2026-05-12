package ru.yandex.practicum.accounts.client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import ru.yandex.practicum.accounts.dto.NotificationDto;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureStubRunner(
        ids = {"ru.yandex.practicum:notifications:+:stubs:8085"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
public class NotificationClientTest {
    static {
        System.setProperty("spring.cloud.loadbalancer.enabled", "false");
        System.setProperty("stubrunner.cloud.enabled", "false");
    }

    @Autowired
    private NotificationClient notificationClient;

    @Test
    void testSend() {
        notificationClient.send(new NotificationDto("message"));
    }
}
