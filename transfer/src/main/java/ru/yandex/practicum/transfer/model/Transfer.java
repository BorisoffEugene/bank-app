package ru.yandex.practicum.transfer.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "transfer", name = "transfer")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, insertable = false, columnDefinition = "timestamp default localtimestamp")
    private LocalDateTime createDate;

    @Column(nullable = false)
    private String accountFrom;

    @Column(nullable = false)
    private String accountTo;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, insertable = false, columnDefinition = "varchar default 'OK'")
    private String status;

    private String error;
}
