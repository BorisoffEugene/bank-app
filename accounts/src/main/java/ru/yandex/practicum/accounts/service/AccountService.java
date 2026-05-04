package ru.yandex.practicum.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.mapper.AccountMapper;
import ru.yandex.practicum.accounts.model.Account;
import ru.yandex.practicum.accounts.repository.AccountRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;

    public AccountResponseDto save(AccountRequestDto dto) {
        Account account = mapper.toEntity(dto);
        Account saved = repository.save(account);
        return mapper.toDto(saved);
    }

    public AccountResponseDto findByLogin(String login) {
        return repository.findByLogin(login)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Ваши данные отсутствуют в базе. Заполните данные и нажмите 'Сохранить'"));
    }

    public List<AccountResponseDto> findOtherAccounts(String login) {
        return repository.findByLoginNot(login)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
}
