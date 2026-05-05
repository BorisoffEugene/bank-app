package ru.yandex.practicum.cash.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class AccountClient {
    private final WebClient accountWebClient;
    private String accountBaseUrl;

    public AccountClient(WebClient accountWebClient, @Value("${bank.accounts.base-url}") String accountBaseUrl) {
        this.accountWebClient = accountWebClient;
        this.accountBaseUrl = accountBaseUrl;
    }
}
