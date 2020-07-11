package com.immccc.bank.transaction.status;

import com.immccc.bank.transaction.Transaction;
import com.immccc.bank.transaction.TransactionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.stream.Stream;

import static com.immccc.bank.transaction.status.Channel.ATM;
import static com.immccc.bank.transaction.status.Channel.CLIENT;
import static com.immccc.bank.transaction.status.Channel.INTERNAL;
import static com.immccc.bank.transaction.status.Status.INVALID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TransactionStatusServiceTest {

    private static final ZonedDateTime TODAY = ZonedDateTime.now();
    private static final String TRANSACTION_REFERENCE = "1234";

    @Mock
    private TransactionService transactionService;

    @Mock
    private TransactionStatusMapper mapper;

    @InjectMocks
    private TransactionStatusService service;

    @DisplayName("Should return an invalid result when transaction does not exist")
    @Test
    void getWithoutTransaction() {
        TransactionStatusQuery query = givenAQuery();
        givenTransactionDoesNotExist();

        TransactionStatus transactionStatus = service.get(query, TODAY);


        thenTransactionStatusIsInvalid(transactionStatus);
    }

    static Stream<Arguments> getWithTransactionTestParametersProvider() {
        return Stream.of(
                arguments(CLIENT, CLIENT),
                arguments(ATM, ATM),
                arguments(INTERNAL, INTERNAL),
                arguments(null, CLIENT));
    }

    @DisplayName("Should return transaction status from transaction")
    @ParameterizedTest(name = "When channel is {0}, transaction status comes from channel {1}")
    @MethodSource("getWithTransactionTestParametersProvider")
    void getWithTransaction(Channel sourceChannel, Channel expectedChannelOnResult) {

        TransactionStatusQuery query = givenAQuery(sourceChannel);
        Transaction transaction = givenAStoredTransaction();

        service.get(query, TODAY);

        verify(mapper).from(eq(expectedChannelOnResult), eq(transaction), any(Status.class));

    }


    private void thenTransactionStatusIsInvalid(TransactionStatus transactionStatus) {
        assertThat(transactionStatus.getReference()).isEqualTo(TRANSACTION_REFERENCE);
        assertThat(transactionStatus.getStatus()).isEqualTo(INVALID);
        assertThat(transactionStatus.getAmount()).isNull();
        assertThat(transactionStatus.getFee()).isNull();
    }

    private void givenTransactionDoesNotExist() {
        doReturn(Optional.empty()).when(transactionService).find(TRANSACTION_REFERENCE);
    }

    private TransactionStatusQuery givenAQuery() {
        return TransactionStatusQuery.builder()
                .reference(TRANSACTION_REFERENCE)
                .build();
    }

    private TransactionStatusQuery givenAQuery(Channel channel) {
        return TransactionStatusQuery.builder()
                .reference(TRANSACTION_REFERENCE)
                .channel(channel)
                .build();
    }

    private Transaction givenAStoredTransaction() {
        Transaction transaction =  Transaction.builder()
                .reference(TRANSACTION_REFERENCE)
                .date(TODAY)
                .build();

        doReturn(Optional.of(transaction)).when(transactionService)
                .find(TRANSACTION_REFERENCE);

        return transaction;
    }
}