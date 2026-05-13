package ru.yandex.practicum.transfer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.transfer.dto.TransferRequestDto;
import ru.yandex.practicum.transfer.dto.TransferResponseDto;
import ru.yandex.practicum.transfer.service.TransferService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    @PreAuthorize("hasRole('USER') && hasAuthority('transfer.write')")
    public TransferResponseDto save(@RequestBody @Valid TransferRequestDto dto, @AuthenticationPrincipal Jwt jwt) {
        dto.setAccountFrom(jwt.getClaimAsString("preferred_username"));
        return transferService.save(dto);
    }
}
