package ru.yandex.practicum.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.accounts.model.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByLogin(String login);
}
