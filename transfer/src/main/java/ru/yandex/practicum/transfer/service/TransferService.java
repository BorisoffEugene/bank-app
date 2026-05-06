package ru.yandex.practicum.transfer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.transfer.client.NotificationClient;
import ru.yandex.practicum.transfer.dto.NotificationDto;
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
    private final NotificationClient notificationClient;

    public TransferResponseDto save(TransferRequestDto dto) {
        notificationClient.send(new NotificationDto(String.format(
                "Перевод со счета %s на счет %s суммы %d руб.",
                dto.getAccountFrom(),
                dto.getAccountTo(),
                dto.getAmount()
        )));

        Transfer transfer = mapper.toEntity(dto);
        Transfer saved = repository.save(transfer);
        return mapper.toDto(saved);
    }
}
