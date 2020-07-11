package com.immccc.bank.account;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(NOT_FOUND)
class AccountNotExistingException extends RuntimeException {
    public AccountNotExistingException() {
        super("Account does not exist");
    }
}
