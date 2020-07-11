package com.immccc.bank.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.immccc.bank.account.AccountWithoutEnoughFoundsException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static com.immccc.bank.transaction.TransactionSorting.ASCENDING;
import static com.immccc.bank.transaction.TransactionSorting.DESCENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    private static final String ACCOUNT_IBAN = "ESXXXX";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionService service;

    @SneakyThrows
    @DisplayName("Should return 201 when element is created")
    @Test
    void postSuccessful() {
        Transaction transaction = givenATransaction();

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transaction)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(CREATED.value());
    }

    static Stream<? extends RuntimeException> postFailureTestParametersProvider() {
        return Stream.of(new TransactionAlreadyExistingException("nop"),
                new AccountWithoutEnoughFoundsException(0L));
    }

    @SneakyThrows
    @DisplayName("Should return 412 on failure")
    @ParameterizedTest
    @MethodSource("postFailureTestParametersProvider")
    void postFailure(RuntimeException exception) {

        Transaction transaction = givenATransaction();
        doThrow(exception).when(service).create(any());

        MvcResult result = mockMvc.perform(post("/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(transaction)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(PRECONDITION_FAILED.value());
    }


    static Stream<TransactionSorting> findTestParametersProvider() {
        return Stream.of(ASCENDING, DESCENDING, null);
    }

    @SneakyThrows
    @DisplayName("Should return 200 on finding transactions by IBAN")
    @ParameterizedTest
    @MethodSource("findTestParametersProvider")
    void find(TransactionSorting sorting) {

        List<Transaction> transactions = givenTransactionsExisting(sorting);

        String request = givenAFindRequest(sorting);

        MvcResult result = mockMvc.perform(get(request)).andReturn();

        thenResponseContainsTransactions(transactions, result);

    }

    @SneakyThrows
    private void thenResponseContainsTransactions(List<Transaction> transactions, MvcResult result){
        assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(mapper.writeValueAsString(transactions));
    }

    private String givenAFindRequest(TransactionSorting sorting) {
        String request = "/transactions/iban/" + ACCOUNT_IBAN;
        if(sorting != null) {
            request += "?sort=" + sorting;
        }
        return request;
    }

    private List<Transaction> givenTransactionsExisting(TransactionSorting sorting) {
        List<Transaction> transactions = Collections.singletonList(givenATransaction());
        doReturn(transactions).when(service).find(ACCOUNT_IBAN, sorting);
        return transactions;
    }

    private Transaction givenATransaction() {
        return Transaction.builder()
                .reference("ABC")
                .accountIban(ACCOUNT_IBAN)
                .amount(BigDecimal.TEN)
                .date(ZonedDateTime.now())
                .fee(BigDecimal.ONE)
                .build();
    }
}