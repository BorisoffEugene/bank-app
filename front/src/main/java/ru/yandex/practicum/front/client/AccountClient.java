package ru.yandex.practicum.front.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.yandex.practicum.front.controller.dto.AccountRequestDto;
import ru.yandex.practicum.front.controller.dto.AccountResponseDto;

@Component
public class AccountClient {
    private final WebClient gatewayWebClient;
    private final String gatewayBaseUrl;

    public AccountClient(WebClient gatewayWebClient, @Value("${bank.gateway.base-url}") String gatewayBaseUrl) {
        this.gatewayWebClient = gatewayWebClient;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public AccountResponseDto findByLogin(String login) {
        return gatewayWebClient
                .get()
                .uri(gatewayBaseUrl + "/account")
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();
    }

    public AccountResponseDto save(AccountRequestDto request) {
        return gatewayWebClient
                .post()
                .uri(gatewayBaseUrl + "/account")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AccountResponseDto.class)
                .block();
    }
}
