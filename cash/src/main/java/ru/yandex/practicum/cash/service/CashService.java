package ru.yandex.practicum.cash.service;

import io.micrometer.core.instrument.Metrics;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.cash.client.AccountClient;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.dto.NotificationDto;
import ru.yandex.practicum.cash.mapper.CashMapper;
import ru.yandex.practicum.cash.model.Cash;
import ru.yandex.practicum.cash.repository.CashRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class CashService {
    private final CashRepository repository;
    private final CashMapper mapper;
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

    public CashResponseDto save(CashRequestDto dto) {
        String message = String.format(
                "Транзакция %s %s суммы %d руб.",
                dto.getAction().equals("GET") ? "снятия со счета" : "пополнения на счет",
                dto.getAccount(),
                dto.getAmount()
        );
        kafkaTemplate.send("notification", new NotificationDto(message));
        log.info("{}, {}, {}", message, getTraceId(), getSpanId());

        try {
            accountClient.changeSum(dto);
        } catch (Exception e) {
            Cash cash = mapper.toEntity(dto);
            cash.setStatus("ERROR");
            cash.setError(e.getMessage());
            repository.save(cash);

            Metrics.counter("cash_failed_total", "login", dto.getAccount()).increment();

            throw new IllegalArgumentException(e.getMessage());
        }

        Cash cash = mapper.toEntity(dto);
        Cash saved = repository.save(cash);
        return mapper.toDto(saved);
    }
}
