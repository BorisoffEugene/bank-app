package ru.yandex.practicum.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.accounts.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByLogin(String login);
    List<Account> findByLoginNot(String login);

    @Modifying
    @Query("update Account a set a.sum = :sum where a.login = :login")
    int updateSum(@Param("sum") int sum, @Param("login") String login);
}
