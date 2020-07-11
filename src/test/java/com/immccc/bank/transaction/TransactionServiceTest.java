package com.immccc.bank.transaction;

import com.immccc.bank.account.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static java.math.BigDecimal.ONE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private static final String TRANSACTION_REFERENCE = "123";
    private static final String ACCOUNT_IBAN = "ESXXXX...";
    private static final BigDecimal TRANSACTION_AMOUNT = ONE;
    private static final BigDecimal TRANSACTION_FEE = BigDecimal.ZERO;

    @Mock
    private AccountService accountService;

    @Mock
    private TransactionRepository repository;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionService service;

    @DisplayName("Should create a transaction")
    @Test
    void createSuccess() {

        Transaction transaction = givenATransaction();
        givenAnEntity(transaction);

        service.create(transaction);

        thenCurrentExistenceOfTransactionIsChecked(transaction);
        thenBalanceInAccountIsUpdated(transaction);
        thenTransactionIsStored(transaction);
    }


    @DisplayName("Should fail when transaction currently exists")
    @Test
    void createAlreadyExists() {
        Transaction transaction = givenATransaction();
        TransactionEntity entity = givenAnEntity(transaction);

        givenTransactionIsStored(entity);

        Assertions.assertThrows(TransactionAlreadyExistingException.class, () -> service.create(transaction));
    }

    private void givenTransactionIsStored(TransactionEntity entity) {
        doReturn(true).when(repository).existsById(entity.getReference());
    }

    private void thenCurrentExistenceOfTransactionIsChecked(Transaction transaction) {
        verify(repository).existsById(transaction.getReference());
    }

    private void thenTransactionIsStored(Transaction transaction) {
        ArgumentCaptor<TransactionEntity> entityCaptor = ArgumentCaptor.forClass(TransactionEntity.class);
        verify(repository).save(entityCaptor.capture());

        TransactionEntity entitySaved = entityCaptor.getValue();

        assertThat(entitySaved.getReference()).isEqualTo(transaction.getReference());
        assertThat(entitySaved.getAccountIban()).isEqualTo(transaction.getAccountIban());
        assertThat(entitySaved.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(entitySaved.getFee()).isEqualTo(transaction.getFee());
        assertThat(entitySaved.getDate()).isEqualTo(transaction.getDate());
        assertThat(entitySaved.getDescription()).isEqualTo(transaction.getDescription());
    }

    private void thenBalanceInAccountIsUpdated(Transaction transaction) {
        verify(accountService).updateBalance(transaction.getAccountIban(), transaction.getAmount());
    }

    private Transaction givenATransaction() {
        return Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .amount(TRANSACTION_AMOUNT)
                .accountIban(ACCOUNT_IBAN)
                .date(ZonedDateTime.now())
                .fee(TRANSACTION_FEE)
                .build();
    }

    private TransactionEntity givenAnEntity(Transaction transaction) {
        TransactionEntity entity =  TransactionEntity.builder()
                .reference(transaction.getReference())
                .accountIban(transaction.getAccountIban())
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .date(transaction.getDate())
                .description(transaction.getDescription())
                .build();

        doReturn(entity).when(mapper).toEntity(transaction);

        return entity;
    }
}