package ru.yandex.practicum.transfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.mapper.TransferMapper;
import ru.yandex.practicum.transfer.model.Transfer;
import ru.yandex.practicum.transfer.repository.TransferRepository;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository repository;
    private final TransferMapper mapper;

    public TransferResponseDto save(TransferRequestDto dto) {
        Transfer transfer = mapper.toEntity(dto);
        Transfer saved = repository.save(transfer);
        return mapper.toDto(saved);
    }
}
