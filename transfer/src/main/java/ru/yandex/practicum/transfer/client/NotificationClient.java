package ru.yandex.practicum.transfer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.transfer.dto.NotificationDto;

@Component
public class NotificationClient {
    private final WebClient notificationWebClient;

    public NotificationClient(WebClient notificationWebClient) {
        this.notificationWebClient = notificationWebClient;
    }

    public void send(NotificationDto request) {
        notificationWebClient
                .post()
                .uri("http://notification-service/notification")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}