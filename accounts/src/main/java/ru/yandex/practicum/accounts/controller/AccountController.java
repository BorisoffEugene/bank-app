package ru.yandex.practicum.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.dto.CashRequestDto;
import ru.yandex.practicum.accounts.dto.TransferRequestDto;
import ru.yandex.practicum.accounts.service.AccountService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @PostMapping
    @PreAuthorize("hasRole('USER') && hasAuthority('accounts.write')")
    public AccountResponseDto save(@RequestBody @Valid AccountRequestDto dto, @AuthenticationPrincipal Jwt jwt) {
        dto.setLogin(jwt.getClaimAsString("preferred_username"));
        return accountService.save(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public AccountResponseDto findByLogin(@AuthenticationPrincipal Jwt jwt) {
        return accountService.findByLogin(jwt.getClaimAsString("preferred_username"));
    }

    @GetMapping("/other-accounts")
    @PreAuthorize("hasRole('USER')")
    public List<AccountResponseDto> findOtherAccounts(@AuthenticationPrincipal Jwt jwt) {
        return accountService.findOtherAccounts(jwt.getClaimAsString("preferred_username"));
    }

    @PostMapping("/change")
    @PreAuthorize("hasRole('SERVICE') && hasAuthority('accounts.write')")
    public void changeSum(@RequestBody @Valid CashRequestDto dto) {
        accountService.changeSum(dto);
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('SERVICE') && hasAuthority('accounts.write')")
    public void transfer(@RequestBody @Valid TransferRequestDto dto) {
        accountService.transfer(dto);
    }
}
