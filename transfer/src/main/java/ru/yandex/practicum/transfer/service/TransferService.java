package ru.yandex.practicum.transfer.service;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.transfer.client.AccountClient;
import ru.yandex.practicum.transfer.dto.NotificationDto;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.mapper.TransferMapper;
import ru.yandex.practicum.transfer.model.Transfer;
import ru.yandex.practicum.transfer.repository.TransferRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransferService {
    private final TransferRepository repository;
    private final TransferMapper mapper;
    private final AccountClient accountClient;
    private final KafkaTemplate<String, NotificationDto> kafkaTemplate;
    private final Tracer tracer;

    private String getTraceId() {
        if (tracer.currentTraceContext().context() != null) {
            return tracer.currentTraceContext().context().traceId();
        }
        return "";
    }

    private String getSpanId() {
        if (tracer.currentTraceContext().context() != null) {
            return tracer.currentTraceContext().context().spanId();
        }
        return "";
    }

    public TransferResponseDto save(TransferRequestDto dto) {
        String message = String.format(
                "Перевод со счета %s на счет %s суммы %d руб.",
                dto.getAccountFrom(),
                dto.getAccountTo(),
                dto.getAmount()
        );
        kafkaTemplate.send("notification", new NotificationDto(message));
        log.info("{}, {}, {}", message, getTraceId(), getSpanId());

        try {
            accountClient.transfer(dto);
        } catch (Exception e) {
            Transfer trunsfer = mapper.toEntity(dto);
            trunsfer.setStatus("ERROR");
            trunsfer.setError(e.getMessage());
            repository.save(trunsfer);

            Metrics.counter("transfer_failed_total", "sender", dto.getAccountFrom(), "recipient", dto.getAccountTo()).increment();

            throw new IllegalArgumentException(e.getMessage());
        }

        Transfer transfer = mapper.toEntity(dto);
        Transfer saved = repository.save(transfer);
        return mapper.toDto(saved);
    }
}
