package com.immccc.bank.transaction;

import com.immccc.bank.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @Transactional
    void create(Transaction transaction) {
        TransactionEntity entity = mapper.toEntity(transaction);
        if(repository.existsById(entity.getReference())) {
            throw new TransactionAlreadyExistingException(entity.getReference());
        }

        accountService.updateBalance(transaction.getAccountIban(), transaction.getAmount());

        repository.save(entity);
    }

}
