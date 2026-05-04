package ru.yandex.practicum.front.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Controller
public class MainController {
    // TODO: Удалить заглушку, так как используется только для ознакомительных целей
    @Autowired
    private AccountStub accountStub;

    private final AccountClient accountClient;

    public MainController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    private String showMain(Model model, AccountResponseDto response, String errors, String info) {
        model.addAttribute("name", response.name());
        model.addAttribute("birthdate", ((response.birthdate() != null) ? response.birthdate() : LocalDate.now()).format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", response.sum());
        model.addAttribute("errors", errors);
        model.addAttribute("info", info);

        return "main";
    }

    private void addOtherAccounts(Model model) {
        List<AccountResponseDto> accounts;
        try {
            accounts = accountClient.findOtherAccounts();
        } catch (Exception e) {
            accounts = new ArrayList<>();
        }

        model.addAttribute("accounts", accounts);
    }

    @GetMapping
    public String index() {
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        String errors = null;
        AccountResponseDto response;
        try {
             response = accountClient.findByLogin();
        } catch (Exception e) {
            errors = e.getMessage();
            response = new AccountResponseDto(null, null, null, 0);
        }

        addOtherAccounts(model);
        return showMain(model, response, errors, null);
    }

    @PostMapping("/account")
    public String editAccount(Model model, @RequestParam("name") String name, @RequestParam("birthdate") LocalDate birthdate) {
        String errors = null;
        String info = "Данные сохранены";
        AccountResponseDto response;
        try {
            response = accountClient.save(new AccountRequestDto(name, birthdate));
        } catch (Exception e) {
            errors = e.getMessage();
            info = null;
            response = new AccountResponseDto(null, null, null, 0);
        }

        addOtherAccounts(model);
        return showMain(model, response, errors, info);
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
