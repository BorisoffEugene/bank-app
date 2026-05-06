package ru.yandex.practicum.cash.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "cash", name = "cash")
public class Cash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, insertable = false, columnDefinition = "timestamp default localtimestamp")
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String account;

    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, columnDefinition = "varchar default 'OK'")
    private String status;

    private String error;
}
