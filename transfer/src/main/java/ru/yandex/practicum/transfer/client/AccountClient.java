package ru.yandex.practicum.transfer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.transfer.dto.ErrorResponse;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;

@Component
public class AccountClient {
    private final WebClient accountWebClient;
    private String accountBaseUrl;

    public AccountClient(WebClient accountWebClient, @Value("${bank.accounts.base-url}") String accountBaseUrl) {
        this.accountWebClient = accountWebClient;
        this.accountBaseUrl = accountBaseUrl;
    }

    public void transfer(TransferRequestDto request) {
        accountWebClient
                .post()
                .uri(accountBaseUrl + "/account/transfer")
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
