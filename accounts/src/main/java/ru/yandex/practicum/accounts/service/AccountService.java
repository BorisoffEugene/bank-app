package ru.yandex.practicum.accounts.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.accounts.client.NotificationClient;
import ru.yandex.practicum.accounts.dto.*;
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
    private final NotificationClient notificationClient;

    public AccountResponseDto save(AccountRequestDto dto) {
        notificationClient.send(new NotificationDto(String.format("Данные клиента по логину '%s' изменены", dto.getLogin())));


        Account account = mapper.toEntity(dto);
        Account saved = repository.save(account);
        return mapper.toDto(saved);
    }

    public AccountResponseDto findByLogin(String login) {
        notificationClient.send(new NotificationDto(String.format("Запрос данных клиента по логину '%s'", login)));

        return repository.findByLogin(login)
                .map(mapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Ваши данные отсутствуют в базе. Заполните данные и нажмите 'Сохранить'"));
    }

    public List<AccountResponseDto> findOtherAccounts(String login) {
        notificationClient.send(new NotificationDto(String.format("Запрос списка клиентов для перевода денег для логина '%s'", login)));

        return repository.findByLoginNot(login)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void changeSum(CashRequestDto dto) {
        AccountResponseDto response = findByLogin(dto.getAccount());

        int sum = response.getSum();
        if (dto.getAction().equals("GET") && sum < dto.getAmount())
            throw new IllegalArgumentException("Недостаточно средств на счету");

        sum += dto.getAmount() * (dto.getAction().equals("GET") ? -1 : 1);
        repository.updateSum(sum, response.getLogin());
    }

    @Transactional
    public void transfer(TransferRequestDto dto) {
        // снять деньги с отправителя
        AccountResponseDto accountFrom = findByLogin(dto.getAccountFrom());
        if (accountFrom.getSum() < dto.getAmount())
            throw new IllegalArgumentException("Недостаточно средств на счету");
        repository.updateSum(accountFrom.getSum() - dto.getAmount(), accountFrom.getLogin());

        // положить денег получателю
        AccountResponseDto accountTo = findByLogin(dto.getAccountTo());
        repository.updateSum(accountTo.getSum() + dto.getAmount(), accountTo.getLogin());
    }
}
