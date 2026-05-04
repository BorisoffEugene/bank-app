package ru.yandex.practicum.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.service.AccountService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    public AccountResponseDto save(@RequestBody @Valid AccountRequestDto dto, @AuthenticationPrincipal Jwt jwt) {
        dto.setLogin(jwt.getClaimAsString("preferred_username"));
        return accountService.save(dto);
    }

    @GetMapping
    public AccountResponseDto findByLogin(@AuthenticationPrincipal Jwt jwt) {
        return accountService.findByLogin(jwt.getClaimAsString("preferred_username"));
    }

}
