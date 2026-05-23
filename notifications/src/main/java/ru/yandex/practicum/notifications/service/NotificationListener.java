package ru.yandex.practicum.notifications.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.notifications.dto.NotificationDto;

@Service
@Slf4j
public class NotificationListener {
    @KafkaListener(topics = "notification")
    public void listenNotifications(ConsumerRecord<String, NotificationDto> record) {
        log.info(record.value().message());
    }
}
