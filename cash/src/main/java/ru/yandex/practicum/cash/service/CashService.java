package ru.yandex.practicum.cash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.yandex.practicum.cash.client.AccountClient;
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
    private final AccountClient accountClient;

    public CashResponseDto save(CashRequestDto dto) {
        notificationClient.send(new NotificationDto(String.format(
                "Транзакция %s %s суммы %d руб.",
                dto.getAction().equals("GET") ? "снятия со счета" : "пополнения на счет",
                dto.getAccount(),
                dto.getAmount()
        )));

        try {
            accountClient.changeSum(dto);
        } catch (Exception e) {
            Cash cash = mapper.toEntity(dto);
            cash.setStatus("ERROR");
            cash.setError(e.getMessage());
            repository.save(cash);

            throw new IllegalArgumentException(e.getMessage());
        }

        Cash cash = mapper.toEntity(dto);
        Cash saved = repository.save(cash);
        return mapper.toDto(saved);
    }
}
