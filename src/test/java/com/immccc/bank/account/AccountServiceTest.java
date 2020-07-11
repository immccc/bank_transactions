package com.immccc.bank.account;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String ACCOUNT_IBAN = "ESXXXXXXX...";
    private static final BigDecimal ACCOUNT_BALANCE = BigDecimal.ONE;

    @Mock
    private AccountRepository repository;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountService service;

    @Test
    @DisplayName("Should find an account")
    void find() {

        givenAnAccountInDatabase();
        Account actualAccount = service.find(ACCOUNT_IBAN);

        assertThat(actualAccount).isNotNull();
        assertThat(actualAccount.getIban()).isEqualTo(ACCOUNT_IBAN);
    }

    @Test
    @DisplayName("Should throw AccountNotExistingException when account is not found")
    void findNotExisting() {
        givenNoAccountInDB();
        assertThrows(AccountNotExistingException.class, () -> service.find(ACCOUNT_IBAN));
    }


    @Test
    @DisplayName("Should create account")
    void create() {
        Account account = givenAccount();

        service.store(account);

        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(repository).save(entityCaptor.capture());
        assertThat(entityCaptor.getValue().getIban()).isEqualTo(account.getIban());
    }

    private Account givenAccount() {
        Account account =  Account.builder().iban(ACCOUNT_IBAN).balance(ACCOUNT_BALANCE).build();
        AccountEntity entity = AccountEntity.builder().iban(ACCOUNT_IBAN).balance(ACCOUNT_BALANCE).build();

        doReturn(entity).when(mapper).toEntity(account);
        return account;
    }

    private void givenAnAccountInDatabase() {
        AccountEntity entity = AccountEntity.builder().iban(ACCOUNT_IBAN).build();
        doReturn(Optional.of(entity)).when(repository).findByIban(ACCOUNT_IBAN);

        Account account = Account.builder().iban(ACCOUNT_IBAN).build();
        doReturn(account).when(mapper).fromEntity(entity);
    }

    private void givenNoAccountInDB() {
        doReturn(Optional.empty()).when(repository).findByIban(ACCOUNT_IBAN);
    }


}