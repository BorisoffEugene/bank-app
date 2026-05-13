package ru.yandex.practicum.transfer.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.transfer.model.Transfer;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("Интеграционное (DB) тестирование перевода денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class TransferRepositoryTest {
    @Autowired
    private TransferRepository repository;

    @Test
    @DisplayName("Сохранение")
    void saveTest() {
        Transfer transfer = Transfer.builder()
                .accountFrom("ivanov")
                .accountTo("petrov")
                .amount(100)
                .status("OK")
                .build();

        Transfer saved = repository.save(transfer);

        assertEquals(transfer.getAccountFrom(), saved.getAccountFrom(), String.format("Аккаунт источник должен быть: %s", transfer.getAccountFrom()));
        assertEquals(transfer.getAccountTo(), saved.getAccountTo(), String.format("Аккаунт получатель должен быть: %s", transfer.getAccountTo()));
        assertEquals(transfer.getAmount(), saved.getAmount(), String.format("Сумма перевода должна быть: %d", transfer.getAmount()));
        assertEquals(transfer.getStatus(), saved.getStatus(), String.format("Статус должен быть: %s", transfer.getStatus()));
    }
}
