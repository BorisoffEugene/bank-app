package ru.yandex.practicum.accounts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.yandex.practicum.accounts.dto.AccountRequestDto;
import ru.yandex.practicum.accounts.dto.AccountResponseDto;
import ru.yandex.practicum.accounts.model.Account;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "login", ignore = true)
    Account toEntity(AccountRequestDto dto);

    AccountResponseDto toDto(Account user);
}
