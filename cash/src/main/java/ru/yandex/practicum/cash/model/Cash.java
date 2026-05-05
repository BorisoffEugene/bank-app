package ru.yandex.practicum.cash.model;

import jakarta.persistence.*;
import lombok.*;

import ru.yandex.practicum.cash.dto.ActionStatus;
import ru.yandex.practicum.cash.dto.CashAction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "cash", name = "cash")
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "timestamp default localtimestamp")
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private CashAction action;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, columnDefinition = "varchar default 'OK'")
    private ActionStatus status;

    private String error;
}
