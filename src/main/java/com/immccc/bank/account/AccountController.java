package com.immccc.bank.account;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("accounts")
class AccountController {

    private final AccountService service;

    @GetMapping("/{iban}")
    Account find(@PathVariable String iban) {
        return service.find(iban);
    }

    @ResponseStatus(CREATED)
    @PostMapping
    void create(@RequestBody Account account) {
        service.store(account);
    }

}