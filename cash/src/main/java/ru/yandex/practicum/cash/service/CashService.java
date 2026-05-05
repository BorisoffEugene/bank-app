package ru.yandex.practicum.cash.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.mapper.CashMapper;
import ru.yandex.practicum.cash.model.Cash;
import ru.yandex.practicum.cash.repository.CashRepository;

@Service
@RequiredArgsConstructor
public class CashService {
    private final CashRepository repository;
    private final CashMapper mapper;

    public CashResponseDto save(CashRequestDto dto) {
        Cash cash = mapper.toEntity(dto);
        Cash saved = repository.save(cash);
        return mapper.toDto(saved);
    }
}
