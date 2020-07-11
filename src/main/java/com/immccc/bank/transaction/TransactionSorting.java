package com.immccc.bank.transaction;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;

@RequiredArgsConstructor
@Getter
enum TransactionSorting {
    ASCENDING(Sort.Direction.ASC),
    DESCENDING(Sort.Direction.DESC);

    private final Sort.Direction direction;
}
