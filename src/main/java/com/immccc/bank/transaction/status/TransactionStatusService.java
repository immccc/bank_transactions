package com.immccc.bank.transaction.status;

import com.immccc.bank.transaction.Transaction;
import com.immccc.bank.transaction.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

import static com.immccc.bank.transaction.status.Channel.CLIENT;
import static com.immccc.bank.transaction.status.Status.INVALID;

@Service
@RequiredArgsConstructor
class TransactionStatusService {

    private final TransactionService transactionService;
    private final TransactionStatusMapper mapper;

    TransactionStatus get(TransactionStatusQuery query, ZonedDateTime compareFrom) {

        Channel channel = Optional.ofNullable(query.getChannel()).orElse(CLIENT);

        return transactionService
                .find(query.getReference())
                .map(transaction ->
                        mapper.from(channel, transaction, getStatus(transaction, compareFrom)))
                .orElse(buildInvalidStatus(query.getReference()));

    }

    private Status getStatus(Transaction transaction, ZonedDateTime compareFrom) {
        return Status.from(transaction.getDate(), compareFrom);
    }

    private TransactionStatus buildInvalidStatus(String transactionReference) {
        return TransactionStatus.builder()
                .reference(transactionReference)
                .status(INVALID)
                .build();
    }

}
