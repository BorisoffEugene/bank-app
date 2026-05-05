package ru.yandex.practicum.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.cash.model.Cash;

public interface CashRepository extends JpaRepository<Cash, Long>{
}
