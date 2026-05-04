package ru.yandex.practicum.front.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.yandex.practicum.front.controller.dto.AccountRequestDto;
import ru.yandex.practicum.front.controller.dto.AccountResponseDto;
import ru.yandex.practicum.front.controller.dto.ErrorResponse;

import java.util.List;

@Component
public class AccountClient {
    private final WebClient gatewayWebClient;
    private final String gatewayBaseUrl;

    public AccountClient(WebClient gatewayWebClient, @Value("${bank.gateway.base-url}") String gatewayBaseUrl) {
        this.gatewayWebClient = gatewayWebClient;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public AccountResponseDto findByLogin() {
        return gatewayWebClient
                .get()
                .uri(gatewayBaseUrl + "/account")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.message())))
                )
                .bodyToMono(AccountResponseDto.class)
                .block();
    }

    public AccountResponseDto save(AccountRequestDto request) {
        return gatewayWebClient
                .post()
                .uri(gatewayBaseUrl + "/account")
                .bodyValue(request)
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.message())))
                )
                .bodyToMono(AccountResponseDto.class)
                .block();
    }

    public List<AccountResponseDto> findOtherAccounts() {
        return gatewayWebClient
                .get()
                .uri(gatewayBaseUrl + "/account/other-accounts")
                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(ErrorResponse.class)
                                .flatMap(errorBody -> Mono.error(new Exception(errorBody.message())))
                )
                .bodyToFlux(AccountResponseDto.class)
                .collectList()
                .block();
    }
}
