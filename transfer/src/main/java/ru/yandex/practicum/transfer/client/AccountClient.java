package ru.yandex.practicum.transfer.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.transfer.dto.ErrorResponse;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;

@Component
public class AccountClient {
    private final WebClient accountWebClient;

    public AccountClient(WebClient accountWebClient) {
        this.accountWebClient = accountWebClient;
    }

    public void transfer(TransferRequestDto request) {
        accountWebClient
                .post()
                .uri("http://account-service/account/transfer")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.getMessage())))
                )
                .bodyToMono(Void.class)
                .block();
    }
}
