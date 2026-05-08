package ru.yandex.practicum.cash.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.cash.model.Cash;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DisplayName("Интеграционное (DB) тестирование снятия / пополнения денег")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class CashRepositoryTest {
    @Autowired
    private CashRepository repository;

    @Test
    @DisplayName("Сохранение")
    void saveTest() {
        Cash cash = Cash.builder()
                .account("ivanov")
                .action("PUT")
                .amount(100)
                .status("OK")
                .build();

        Cash saved = repository.save(cash);

        assertEquals(cash.getAccount(), saved.getAccount(), String.format("Аккаунт должен быть: %s", cash.getAccount()));
        assertEquals(cash.getAction(), saved.getAction(), String.format("Действие должно быть: %s", cash.getAction()));
        assertEquals(cash.getAmount(), saved.getAmount(), String.format("Сумма пополнения должна быть: %d", cash.getAmount()));
        assertEquals(cash.getStatus(), saved.getStatus(), String.format("Статус должен быть: %s", cash.getStatus()));
    }
}
