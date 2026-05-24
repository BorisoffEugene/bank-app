package ru.yandex.practicum.front.controller;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        addOtherAccounts(model);

        if (response == null) {
            try {
                response = accountClient.findByLogin();
            } catch (Exception e) {
                model.addAttribute("errors", e.getMessage());
                return "main";
            }
        }

        model.addAttribute("name", response.name());
        model.addAttribute("birthdate", (response.birthdate() == null) ? null : response.birthdate().format(DateTimeFormatter.ISO_DATE));
        model.addAttribute("sum", response.sum());

        model.addAttribute("errors", errors);
        model.addAttribute("info", info);

        return "main";
    }

    private String showMain(Model model) {
        return showMain(model, null, null, null);
    }

    private String showMain(Model model, String errors, String info) {
        return showMain(model, null, errors, info);
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
        log.info("{}, {}, {}", "Главная страница", "traceId", "spanId"); // todo
        return "redirect:/account";
    }

    @GetMapping("/account")
    public String getAccount(Model model) {
        return showMain(model);
    }

    @PostMapping("/account")
    public String editAccount(Model model, @RequestParam String name, @RequestParam LocalDate birthdate) {
        String errors = null;
        String info = "Данные сохранены";

        try {
            accountClient.save(new AccountRequestDto(name, birthdate));
        } catch (Exception e) {
            errors = e.getMessage();
            info = null;
        }

        return showMain(model, errors, info);
    }

    @PostMapping("/cash")
    public String editCash(Model model, @RequestParam int value, @RequestParam CashAction action) {
        String errors = null;
        String info = String.format("Успешное %s %d руб.", (action == CashAction.GET) ? "снятие" : "пополнение на", value);

        try {
            cashClient.save(new CashRequestDto(action.name(), value, "OK"));
        } catch (Exception e) {
            errors = e.getMessage();
            info = null;
        }

        return showMain(model, errors, info);
    }

    @PostMapping("/transfer")
    public String transfer(Model model, @RequestParam int value, @RequestParam String login) {
        String errors = null;
        String info = String.format("Успешно переведено %d руб.", value);

        try {
            transferClient.save(new TransferRequestDto(login, value, "OK"));
        } catch (Exception e) {
            errors = e.getMessage();
            info = null;
        }

        return showMain(model, errors, info);
    }
}
