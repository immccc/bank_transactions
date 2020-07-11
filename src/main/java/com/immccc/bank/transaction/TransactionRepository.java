package com.immccc.bank.transaction;

import org.springframework.data.repository.CrudRepository;

interface TransactionRepository extends CrudRepository<TransactionEntity, String> {
}
