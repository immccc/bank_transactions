package com.immccc.bank.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public
class AccountService {

    private final AccountRepository repository;
    private final AccountMapper mapper;

    @Transactional
    public synchronized void updateBalance(String iban, BigDecimal amount) {
        AccountEntity entity = getEntity(iban);
        entity.setBalance(entity.getBalance().add(amount));
        if(entity.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountWithoutEnoughFoundsException(entity.getId());
        }
        repository.save(entity);
    }

    Account find(String iban) {
        AccountEntity entity = getEntity(iban);
        return mapper.fromEntity(entity);
    }

    @Transactional
    void store(Account account) {
        repository.save(mapper.toEntity(account));
    }

    private AccountEntity getEntity(String iban) {
        return repository.findByIban(iban)
                .orElseThrow(AccountNotExistingException::new);
    }

}
