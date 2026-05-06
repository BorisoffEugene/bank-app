package ru.yandex.practicum.front.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.front.controller.dto.ErrorResponse;
import ru.yandex.practicum.front.controller.dto.TransferRequestDto;
import ru.yandex.practicum.front.controller.dto.TransferResponseDto;

@Component
public class TransferClient {
    private final WebClient gatewayWebClient;
    private final String gatewayBaseUrl;

    public TransferClient(WebClient gatewayWebClient, @Value("${bank.gateway.base-url}") String gatewayBaseUrl) {
        this.gatewayWebClient = gatewayWebClient;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public TransferResponseDto save(TransferRequestDto request) {
        return gatewayWebClient
                .post()
                .uri(gatewayBaseUrl + "/transfer")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.message())))
                )
                .bodyToMono(TransferResponseDto.class)
                .block();
    }
}
