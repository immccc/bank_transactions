package com.immccc.bank.transaction;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
class TransactionMapper {


    TransactionEntity toEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .reference(StringUtils.defaultIfBlank(transaction.getReference(), UUID.randomUUID().toString()))
                .accountIban(transaction.getAccountIban())
                .date(Optional.ofNullable(transaction.getDate()).orElse(ZonedDateTime.now()))
                .amount(transaction.getAmount())
                .fee(Optional.ofNullable(transaction.getFee()).orElse(BigDecimal.ZERO))
                .description(transaction.getDescription())
                .build();
    }

}
