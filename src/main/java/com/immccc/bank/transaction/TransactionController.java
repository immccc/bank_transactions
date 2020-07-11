package com.immccc.bank.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions")
class TransactionController {
    
    private final TransactionService service;
    
    @ResponseStatus(CREATED)
    @PostMapping
    void create(@RequestBody Transaction transaction) {
        service.create(transaction);
    }
}
