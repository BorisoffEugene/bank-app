package ru.yandex.practicum.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.front.client.AccountClient;
import ru.yandex.practicum.front.controller.dto.AccountDto;
import ru.yandex.practicum.front.controller.dto.AccountRequestDto;
import ru.yandex.practicum.front.controller.dto.AccountResponseDto;
import ru.yandex.practicum.front.controller.dto.CashAction;
import ru.yandex.practicum.front.controller.stub.AccountStub;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

@Controller
public class MainController {
    // TODO: Удалить заглушку, так как используется только для ознакомительных целей
    @Autowired
    private AccountStub accountStub;

    private final AccountClient accountClient;

    public MainController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String getAccount(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        AccountResponseDto response = accountClient.findByLogin(userDetails.getUsername());

        model.addAttribute("name", response.name());
        model.addAttribute("birthdate", response.birthdate().format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", response.sum());
        model.addAttribute("accounts", new ArrayList<AccountDto>()); // todo
        model.addAttribute("errors", null); // todo
        model.addAttribute("info", null); // todo

        return "main";
    }

    @PostMapping("/account")
    public String editAccount(
            Model model,
            @RequestParam("name") String name, @RequestParam("birthdate") LocalDate birthdate,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        AccountResponseDto response = accountClient.save(new AccountRequestDto("ivanov", name, birthdate));

        model.addAttribute("name", response.name());
        model.addAttribute("birthdate", response.birthdate().format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", response.sum());
        model.addAttribute("accounts", new ArrayList<AccountDto>()); // todo
        model.addAttribute("errors", null); // todo
        model.addAttribute("info", null); // todo

        return "main";
    }

    /**
     * POST /cash.
     * Что нужно сделать:
     * 1. Сходить в сервис cash через Gateway API для снятия/пополнения счета текущего аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     *
     * Параметры:
     * 1. value - сумма списания
     * 2. action - GET (снять), PUT (пополнить)
     */
    @PostMapping("/cash")
    public String editCash(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("action") CashAction action
            ) {
        // TODO: Заменить на то, что описано в комментарии к методу
        accountStub.editCash(model, value, action);

        return "main";
    }

    /**
     * POST /transfer.
     * Что нужно сделать:
     * 1. Сходить в сервис accounts через Gateway API для перевода со счета текущего аккаунта на счет другого аккаунта по REST
     * 2. Заполнить модель main.html полученными из ответа данными
     * 3. Текущего пользователя можно получить из контекста Security
     *
     * Параметры:
     * 1. value - сумма списания
     * 2. login - логин пользователя получателя
     */
    @PostMapping("/transfer")
    public String transfer(
            Model model,
            @RequestParam("value") int value,
            @RequestParam("login") String login
    ) {
        // TODO: Заменить на то, что описано в комментарии к методу
        accountStub.transfer(model, value, login);

        return "main";
    }
}
