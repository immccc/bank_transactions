package com.immccc.bank.account;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final Long ACCOUNT_INTERNAL_ID = 1L;
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

        AccountEntity entity = givenAnAccountInDatabase(BigDecimal.ZERO);
        givenAMappingFromEntity(entity);
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

    static Stream<Arguments> updateBalanceTestParametersProvider() {
        return Stream.of(
                arguments(BigDecimal.TEN, BigDecimal.ONE.negate(), BigDecimal.TEN.subtract(BigDecimal.ONE), false),
                arguments(BigDecimal.TEN, BigDecimal.ONE, BigDecimal.TEN.add(BigDecimal.ONE), false),
                arguments(BigDecimal.ONE, BigDecimal.TEN.negate(), null, true)
        );
    }
    @DisplayName("Update balance")
    @ParameterizedTest(name = "should be {2} in account when balance is {0} and amount {1} is added, and error presence is {3}")
    @MethodSource("updateBalanceTestParametersProvider")
    void updateBalance(BigDecimal originalBalance, BigDecimal amountToAdd,
                       BigDecimal expectedBalanceAfter, boolean expectedError) {

        givenAnAccountInDatabase(originalBalance);

        try {
            service.updateBalance(ACCOUNT_IBAN, amountToAdd);
        } catch(AccountWithoutEnoughFoundsException e) {
            assertThat(expectedError).isTrue();
            return;
        }

        assertThat(expectedError).isFalse();
        ArgumentCaptor<AccountEntity> entityCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(repository).save(entityCaptor.capture());

        assertThat(entityCaptor.getValue().getBalance()).isEqualTo(expectedBalanceAfter);
    }


    private Account givenAccount() {
        Account account =  Account.builder().iban(ACCOUNT_IBAN).balance(ACCOUNT_BALANCE).build();
        AccountEntity entity = AccountEntity.builder().iban(ACCOUNT_IBAN).balance(ACCOUNT_BALANCE).build();

        doReturn(entity).when(mapper).toEntity(account);
        return account;
    }

    private AccountEntity givenAnAccountInDatabase(BigDecimal balance) {
        AccountEntity entity = AccountEntity.builder()
                .id(ACCOUNT_INTERNAL_ID)
                .iban(ACCOUNT_IBAN)
                .balance(balance)
                .build();
        doReturn(Optional.of(entity)).when(repository).findByIban(ACCOUNT_IBAN);

        return entity;
    }

    private void givenNoAccountInDB() {
        doReturn(Optional.empty()).when(repository).findByIban(ACCOUNT_IBAN);
    }

    private void givenAMappingFromEntity(AccountEntity entity) {
        Account account = Account.builder().iban(entity.getIban())
                .balance(entity.getBalance()).build();
        doReturn(account).when(mapper).fromEntity(entity);

    }


}