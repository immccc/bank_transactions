package com.immccc.bank.transaction.status;

import com.immccc.bank.transaction.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static com.immccc.bank.transaction.status.Channel.ATM;
import static com.immccc.bank.transaction.status.Channel.CLIENT;
import static com.immccc.bank.transaction.status.Channel.INTERNAL;
import static com.immccc.bank.transaction.status.Status.PENDING;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@ExtendWith(MockitoExtension.class)
class TransactionStatusMapperTest {

    private static final String TRANSACTION_REFERENCE = "123";
    private static final String ACCOUNT_IBAN = "ESXXXX...";
    private static final BigDecimal TRANSACTION_AMOUNT = TEN;
    private static final BigDecimal TRANSACTION_FEE = ONE;
    private static final Status TRANSACTION_STATUS = PENDING;

    @InjectMocks
    private TransactionStatusMapper mapper;

    static Stream<Arguments> fromTestParametersProvider() {
        return Stream.of(
                arguments(ATM, TRANSACTION_AMOUNT.subtract(TRANSACTION_FEE), null),
                arguments(CLIENT, TRANSACTION_AMOUNT.subtract(TRANSACTION_FEE), null),
                arguments(INTERNAL, TRANSACTION_AMOUNT, TRANSACTION_FEE)
        );
    }

    @DisplayName("Should map transaction status according to channel and transaction")
    @ParameterizedTest(name = "Channel {0} should show amount {1} and fee {2}")
    @MethodSource("fromTestParametersProvider")
    void from(Channel channel, BigDecimal expectedAmount, BigDecimal expectedFee) {
        Transaction transaction = givenATransaction();

        TransactionStatus transactionStatus = mapper.from(channel, transaction, TRANSACTION_STATUS);

        assertThat(transactionStatus.getStatus()).isEqualTo(TRANSACTION_STATUS);
        assertThat(transactionStatus.getReference()).isEqualTo(TRANSACTION_REFERENCE);
        assertThat(transactionStatus.getAmount()).isEqualTo(expectedAmount);
        assertThat(transactionStatus.getFee()).isEqualTo(expectedFee);

    }

    private Transaction givenATransaction() {
        return Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .accountIban(ACCOUNT_IBAN)
                .amount(TRANSACTION_AMOUNT)
                .fee(TRANSACTION_FEE)
                .build();
    }

}