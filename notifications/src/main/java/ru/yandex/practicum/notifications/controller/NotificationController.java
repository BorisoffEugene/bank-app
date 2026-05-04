package ru.yandex.practicum.notifications.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.notifications.dto.NotificationDto;
import ru.yandex.practicum.notifications.service.NotificationLogService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final NotificationLogService notificationLogService;

    @PostMapping
    //@PreAuthorize("hasRole('SERVICE') && hasAuthority('notifications.write')") todo
    public void send(@RequestBody NotificationDto dto) {
        notificationLogService.send(dto);
    }
}
