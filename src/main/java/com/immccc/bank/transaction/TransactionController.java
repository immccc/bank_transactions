package com.immccc.bank.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/iban/{iban}")
    List<Transaction> get(
            @PathVariable String iban, @RequestParam(name="sort", required = false) TransactionSorting sorting) {
        return service.find(iban, sorting);
    }
}
