package ru.yandex.practicum.accounts.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.yandex.practicum.accounts.model.Account;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Интеграционное (DB) тестирование аккаунта")
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class AccountRepositoryTest {
    @Autowired
    private AccountRepository repository;

    private Account account1;
    private Account account2;
    private Account account3;

    @BeforeEach
    void beforeEach() {
        repository.deleteAll();

        account1 = Account.builder()
                .login("ivanov")
                .name("Иванов Иван")
                .birthdate(LocalDate.of(2000, 3, 12))
                .build();
        account2 = Account.builder()
                .login("petrov")
                .name("Петр Петрович")
                .birthdate(LocalDate.of(2000, 3, 12))
                .build();
        account3 = Account.builder()
                .login("sidorov")
                .name("Сидоров Сидор")
                .birthdate(LocalDate.of(2001, 6, 15))
                .build();

        account2 = repository.save(account2);
        account3 = repository.save(account3);
    }

    @Test
    @DisplayName("Сохранение")
    void saveTest() {
        Account saved = repository.save(account1);

        assertEquals(account1.getLogin(), saved.getLogin(), String.format("Логин должен быть: %s", account1.getLogin()));
        assertEquals(account1.getName(), saved.getName(), String.format("Имя должно быть: %s", account1.getName()));
        assertEquals(account1.getBirthdate(), saved.getBirthdate(), String.format("Дата рождения должна быть: %tF", account1.getBirthdate()));
    }

    @Test
    @DisplayName("Получение данных аккаунта (данные есть)")
    void testFindByLogin_Success() {
        Optional<Account> optional = repository.findByLogin(account2.getLogin());
        assertTrue(optional.isPresent(), "Данные должны быть");

        Account found = optional.get();
        assertEquals(account2.getLogin(), found.getLogin(), String.format("Логин должен быть: %s", account2.getLogin()));
        assertEquals(account2.getName(), found.getName(), String.format("Имя должно быть: %s", account2.getName()));
        assertEquals(account2.getBirthdate(), found.getBirthdate(), String.format("Дата рождения должна быть: %tF", account2.getBirthdate()));
    }

    @Test
    @DisplayName("Получение данных аккаунта (данных нет)")
    void testFindByLogin_NotFound() {
        Optional<Account> optional = repository.findByLogin("wrong_login");

        assertFalse(optional.isPresent(), "Данных не должно быть");
    }

    @Test
    @DisplayName("Получение остальных аккаунтов (данные есть)")
    void testFindOtherAccounts_Success() {
        List<Account> list = repository.findByLoginNot(account2.getLogin());

        assertEquals(1, list.size(), String.format("Аккаунтов должно быть: %d", 1));
        assertEquals(list.getFirst().getLogin(), account3.getLogin(), String.format("Логин должен быть: %s", list.getFirst().getLogin()));
        assertEquals(list.getFirst().getName(), account3.getName(), String.format("Имя должно быть: %s", list.getFirst().getName()));
        assertEquals(list.getFirst().getBirthdate(), account3.getBirthdate(), String.format("Дата рождения должна быть: %tF", list.getFirst().getBirthdate()));
    }

    @Test
    @DisplayName("Получение остальных аккаунтов (данных нет)")
    void testFindOtherAccounts_NotFound() {
        repository.deleteAll();
        List<Account> list = repository.findByLoginNot(account2.getLogin());

        assertEquals(0, list.size(), String.format("Аккаунтов должно быть: %d", 0));
    }
}
