package ru.yandex.practicum.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public AccountResponseDto save(@RequestBody @Valid AccountRequestDto dto) {
        return accountService.save(dto);
    }

    @GetMapping
    public AccountResponseDto findByLogin(String login) {
        return accountService.findByLogin("ivanov");
    }

}
