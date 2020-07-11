package com.immccc.bank.transaction.status;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static com.immccc.bank.transaction.status.Status.FUTURE;
import static com.immccc.bank.transaction.status.Status.INVALID;
import static com.immccc.bank.transaction.status.Status.PENDING;
import static com.immccc.bank.transaction.status.Status.SETTLED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(TransactionStatusController.class)
class TransactionStatusControllerTest {

    private static final String TRANSACTION_REFERENCE = "12354";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private TransactionStatusService service;

    static Stream<Arguments> getTestParametersProvider() {
        return Stream.of(
                arguments(PENDING, OK),
                arguments(FUTURE, OK),
                arguments(SETTLED, OK),
                arguments(INVALID, NOT_FOUND));
    }

    @SneakyThrows
    @DisplayName("Should return HTTP status according to transaction status")
    @ParameterizedTest(name = "When transaction status is {0}, http status is {1}")
    @MethodSource("getTestParametersProvider")
    void getTransactionStatus(Status status, HttpStatus expectedHttpStatus) {

        TransactionStatusQuery query = givenAQuery();
        TransactionStatus transactionStatus =  givenTransactionStatus(query, status);


        MvcResult mvcResult = mockMvc.perform(get("/transactions/status")
                .contentType(APPLICATION_JSON)
                .content(mapper.writeValueAsString(query)))
                .andReturn();

        assertThat(mvcResult.getResponse().getStatus())
                .isEqualTo(expectedHttpStatus.value());
        assertThat(mvcResult.getResponse().getContentAsString())
                .isEqualTo(mapper.writeValueAsString(transactionStatus));
    }

    private TransactionStatus givenTransactionStatus(TransactionStatusQuery query, Status status) {
        TransactionStatus transactionStatus = TransactionStatus.builder()
                .reference(TRANSACTION_REFERENCE)
                .status(status)
                .build();

        doReturn(transactionStatus).when(service).get(eq(query), any(ZonedDateTime.class));

        return transactionStatus;
    }


    private TransactionStatusQuery givenAQuery() {
        return TransactionStatusQuery.builder()
                .reference(TRANSACTION_REFERENCE)
                .build();
    }
}