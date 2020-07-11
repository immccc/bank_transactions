package com.immccc.bank.transaction;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface TransactionRepository extends CrudRepository<TransactionEntity, String> {

    List<TransactionEntity> findByAccountIban(String iban, Sort sort);
}
