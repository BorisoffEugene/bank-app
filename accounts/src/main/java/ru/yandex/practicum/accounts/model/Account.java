package ru.yandex.practicum.accounts.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(schema = "accounts", name = "accounts")
public class Account {
    @Id
    private String login;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false, insertable = false, updatable = false, columnDefinition = "int default 0")
    private int sum;
}
