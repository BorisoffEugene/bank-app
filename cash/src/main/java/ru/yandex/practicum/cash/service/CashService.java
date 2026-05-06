package ru.yandex.practicum.cash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.cash.client.NotificationClient;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.dto.NotificationDto;
import ru.yandex.practicum.cash.mapper.CashMapper;
import ru.yandex.practicum.cash.model.Cash;
import ru.yandex.practicum.cash.repository.CashRepository;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashRepository repository;
    private final CashMapper mapper;
    private final NotificationClient notificationClient;

    public CashResponseDto save(CashRequestDto dto) {
        notificationClient.send(new NotificationDto(String.format(
                "Транзакция %s %s суммы %d руб.",
                dto.getAction().equals("GET") ? "снятия со счета" : "пополнения на счет",
                dto.getAccount(),
                dto.getAmount()
        )));

        if (dto.getAction().equals("PUT")) {

        }

        Cash cash = mapper.toEntity(dto);
        Cash saved = repository.save(cash);
        return mapper.toDto(saved);
    }
}
