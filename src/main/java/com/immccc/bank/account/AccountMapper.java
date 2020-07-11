package com.immccc.bank.account;

import org.springframework.stereotype.Component;

@Component
class AccountMapper {


    Account fromEntity(AccountEntity accountEntity) {
        return Account.builder()
                .iban(accountEntity.getIban())
                .balance(accountEntity.getBalance())
                .build();
    }

    AccountEntity toEntity(Account account) {
        return AccountEntity.builder()
                .iban(account.getIban())
                .balance(account.getBalance())
                .build();
    }

}
