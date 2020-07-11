package com.immccc.bank.transaction;

import com.immccc.bank.account.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public
class TransactionService {

    private final AccountService accountService;
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    public Optional<Transaction> find(String reference) {
        return repository.findById(reference)
                .map(mapper::fromEntity);
    }

    List<Transaction> find(String iban, TransactionSorting sorting) {

        Sort sort = Sort.by(Optional.ofNullable(sorting)
                .map(TransactionSorting::getDirection)
                .orElse(Sort.Direction.ASC), "amount");
        return repository.findByAccountIban(iban, sort).stream()
                .map(mapper::fromEntity)
                .collect(Collectors.toList());
    }

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
