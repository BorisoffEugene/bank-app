package ru.yandex.practicum.front.client;

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

    public AccountClient(WebClient gatewayWebClient) {
        this.gatewayWebClient = gatewayWebClient;
    }

    public AccountResponseDto findByLogin() {
        return gatewayWebClient
                .get()
                .uri("http://gateway-service/account")
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
                .uri("http://gateway-service/account")
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
                .uri("http://gateway-service/account/other-accounts")
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
