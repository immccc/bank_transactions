package com.immccc.bank.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    Account find(String iban) {
        return repository.findByIban(iban)
                .map(mapper::fromEntity)
                .orElseThrow(AccountNotExistingException::new);
    }

    @Transactional
    void store(Account account) {
        repository.save(mapper.toEntity(account));
    }


}
