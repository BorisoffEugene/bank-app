package ru.yandex.practicum.notifications.service;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.notifications.dto.NotificationDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {
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

    @KafkaListener(topics = "notification")
    public void listenNotifications(ConsumerRecord<String, NotificationDto> record) {
        log.info("{}, {}, {}", record.value().message(), getTraceId(), getSpanId());
    }
}
