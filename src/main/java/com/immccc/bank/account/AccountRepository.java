package com.immccc.bank.account;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    Optional<AccountEntity> findByIban(String iban);
}
