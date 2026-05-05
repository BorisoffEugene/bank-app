package ru.yandex.practicum.transfer.mapper;

import org.mapstruct.Mapper;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.model.Transfer;

@Mapper(componentModel = "spring")
public interface TransferMapper {
    Transfer toEntity(TransferRequestDto dto);
    TransferResponseDto toDto(Transfer transfer);
}
