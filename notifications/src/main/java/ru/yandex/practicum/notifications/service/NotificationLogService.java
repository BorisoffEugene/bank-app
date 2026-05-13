package ru.yandex.practicum.notifications.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.notifications.dto.NotificationDto;

@Service
@Slf4j
public class NotificationLogService {
    public void send(NotificationDto dto) {
        log.info(dto.message());
    }
}
