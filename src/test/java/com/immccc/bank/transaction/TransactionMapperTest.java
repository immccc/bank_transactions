package com.immccc.bank.transaction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionMapperTest {

    private TransactionMapper mapper = new TransactionMapper();

    @DisplayName("Should map an account to an entity")
    @Test
    void toEntity() {
        Transaction transaction = givenAFilledTransaction();

        TransactionEntity entity = mapper.toEntity(transaction);

        assertThat(entity.getReference()).isEqualTo(transaction.getReference());
        assertThat(entity.getAccountIban()).isEqualTo(transaction.getAccountIban());
        assertThat(entity.getDate()).isEqualTo(transaction.getDate());
        assertThat(entity.getAmount()).isEqualTo(transaction.getAmount());
        assertThat(entity.getFee()).isEqualTo(transaction.getFee());
        assertThat(entity.getDescription()).isEqualTo(transaction.getDescription());
    }
    
    @DisplayName("Should map an account to an entity and fill the optional fields")
    @Test
    void toEntityWithDefaults() {
        Transaction transaction = givenAnEmptyTransaction();

        TransactionEntity entity = mapper.toEntity(transaction);

        assertThat(entity.getReference()).isNotBlank();
        assertThat(entity.getFee()).isEqualTo(BigDecimal.ZERO);
        assertThat(entity.getDate()).isNotNull();
    }

    private Transaction givenAnEmptyTransaction() {
        return Transaction.builder().build();
    }

    private Transaction givenAFilledTransaction() {
        return Transaction.builder()
                .reference("123")
                .accountIban("ESXXX")
                .date(ZonedDateTime.now())
                .amount(TEN)
                .fee(ONE)
                .description("asd")
                .build();
    }

}