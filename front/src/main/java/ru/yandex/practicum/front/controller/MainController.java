package ru.yandex.practicum.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yandex.practicum.front.client.AccountClient;
import ru.yandex.practicum.front.client.CashClient;
import ru.yandex.practicum.front.client.TransferClient;
import ru.yandex.practicum.front.controller.dto.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {
    private final AccountClient accountClient;
    private final CashClient cashClient;
    private final TransferClient transferClient;

    public MainController(AccountClient accountClient, CashClient cashClient, TransferClient transferClient) {
        this.accountClient = accountClient;
        this.cashClient = cashClient;
        this.transferClient = transferClient;
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

    @PostMapping("/cash")
    public String editCash(Model model, @RequestParam("value") int value, @RequestParam("action") CashAction action) {
        CashResponseDto response = cashClient.save(new CashRequestDto(action.name(), value));
        return "main";
    }

    @PostMapping("/transfer")
    public String transfer(Model model, @RequestParam("value") int value, @RequestParam("login") String login) {
        TransferResponseDto response = transferClient.save(new TransferRequestDto(login, value));

        return "main";
    }
}
