package ru.yandex.practicum.transfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.transfer.model.Transfer;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
}
