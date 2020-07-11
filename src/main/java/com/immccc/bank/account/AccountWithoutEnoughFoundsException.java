package com.immccc.bank.account;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ResponseStatus(PRECONDITION_FAILED)
public class AccountWithoutEnoughFoundsException extends RuntimeException {
    public AccountWithoutEnoughFoundsException(long accountId) {
        super(String.format("Cannot subtract money from account %d", accountId));
    }
}
