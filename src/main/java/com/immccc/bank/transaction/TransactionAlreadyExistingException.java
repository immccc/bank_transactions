package com.immccc.bank.transaction;

import org.springframework.web.bind.annotation.ResponseStatus;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;

@ResponseStatus(PRECONDITION_FAILED)
class TransactionAlreadyExistingException extends RuntimeException {

    public TransactionAlreadyExistingException(String reference) {
        super(format("Transaction %s already exist", reference));
    }
}
