package ru.yandex.practicum.front.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.front.controller.dto.CashRequestDto;
import ru.yandex.practicum.front.controller.dto.CashResponseDto;
import ru.yandex.practicum.front.controller.dto.ErrorResponse;

@Component
public class CashClient {
    private final WebClient gatewayWebClient;
    private final String gatewayBaseUrl;

    public CashClient(WebClient gatewayWebClient, @Value("${bank.gateway.base-url}") String gatewayBaseUrl) {
        this.gatewayWebClient = gatewayWebClient;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public CashResponseDto save(CashRequestDto request) {
        return gatewayWebClient
                .post()
                .uri(gatewayBaseUrl + "/cash")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.message())))
                )
                .bodyToMono(CashResponseDto.class)
                .block();
    }
}
