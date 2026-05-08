package ru.yandex.practicum.cash.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.model.Cash;

@Mapper(componentModel = "spring")
public interface CashMapper {
    Cash toEntity(CashRequestDto dto);
    CashResponseDto toDto(Cash cash);
}
