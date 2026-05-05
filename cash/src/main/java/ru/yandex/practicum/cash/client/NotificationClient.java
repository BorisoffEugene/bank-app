package ru.yandex.practicum.cash.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.cash.dto.NotificationDto;

@Component
public class NotificationClient {
    private final WebClient notificationWebClient;
    private String notificationBaseUrl;

    public NotificationClient(WebClient notificationWebClient, @Value("${bank.notifications.base-url}") String notificationBaseUrl) {
        this.notificationWebClient = notificationWebClient;
        this.notificationBaseUrl = notificationBaseUrl;
    }

    public void send(NotificationDto request) {
        notificationWebClient
                .post()
                .uri(notificationBaseUrl + "/notification")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}