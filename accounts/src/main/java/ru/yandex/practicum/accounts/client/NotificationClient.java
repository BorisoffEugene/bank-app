package ru.yandex.practicum.accounts.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.accounts.dto.NotificationDto;

@Component
public class NotificationClient {
    private final WebClient notificationWebClient;

    public NotificationClient(WebClient notificationWebClient) {
        this.notificationWebClient = notificationWebClient;
    }

    public void send(NotificationDto request) {
        notificationWebClient
                .post()
                .uri("http://localhost:8085/notification")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
