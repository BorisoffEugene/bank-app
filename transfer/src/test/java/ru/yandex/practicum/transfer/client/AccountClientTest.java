package ru.yandex.practicum.transfer.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "stubrunner.ids-to-service-ids.accounts=account-service"
)
@AutoConfigureStubRunner(
        ids = {"ru.yandex.practicum:accounts:+:stubs:8082"},
        stubsMode = StubRunnerProperties.StubsMode.LOCAL
)
@DisplayName("Контрактное тестирование перевода суммы")
public class AccountClientTest {
    @Autowired
    private AccountClient accountClient;

    @Test
    @DisplayName("Перевод")
    void testSend() {
        assertDoesNotThrow(() -> accountClient.transfer(new TransferRequestDto("ivanov", "petrov", 100, "OK")));
    }
}