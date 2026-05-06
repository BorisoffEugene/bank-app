package ru.yandex.practicum.cash.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.cash.dto.CashRequestDto;
import ru.yandex.practicum.cash.dto.CashResponseDto;
import ru.yandex.practicum.cash.service.CashService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cash")
public class CashController {
    private final CashService cashService;

    @PostMapping
    @PreAuthorize("hasRole('USER') && hasAuthority('cash.write')")
    public CashResponseDto save(@RequestBody @Valid CashRequestDto dto, @AuthenticationPrincipal Jwt jwt) {
        dto.setAccount(jwt.getClaimAsString("preferred_username"));
        return cashService.save(dto);
    }
}
