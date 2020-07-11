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
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.PRECONDITION_FAILED;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

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

    private Transaction givenATransaction() {
        return Transaction.builder()
                .reference("ABC")
                .accountIban("ESXXXX")
                .amount(BigDecimal.TEN)
                .date(ZonedDateTime.now())
                .fee(BigDecimal.ONE)
                .build();
    }
}