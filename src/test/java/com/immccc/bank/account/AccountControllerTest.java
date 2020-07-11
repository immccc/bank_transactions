package com.immccc.bank.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(AccountController.class)
class AccountControllerTest {
    private static final String ACCOUNT_IBAN = "ESXXXX...";
    private static final BigDecimal ACCOUNT_BALANCE = BigDecimal.ONE;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private AccountService service;

    @SneakyThrows
    @DisplayName("Should return 200 when element in GET is found")
    @Test
    void getSuccessful() {
        Account account = givenAccount();
        Mockito.doReturn(account).when(service).find(ACCOUNT_IBAN);

        MvcResult result = mockMvc.perform(get("/accounts/ " + ACCOUNT_IBAN)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
    }

    @SneakyThrows
    @DisplayName("Should return 404 when element in GET is not found")
    @Test
    void getFailed() {
        doThrow(new AccountNotExistingException()).when(service).find(any());

        MvcResult result = mockMvc.perform(get("/accounts/ " + ACCOUNT_IBAN)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
    }


    @SneakyThrows
    @DisplayName("Should return 201 when accunt is created")
    @Test
    void create() {
        Account account = givenAccount();
        MvcResult result = mockMvc.perform(post("/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(account)))
                .andReturn();

        assertThat(result.getResponse().getStatus()).isEqualTo(CREATED.value());
    }

    private Account givenAccount() {
        return Account.builder()
                .iban(ACCOUNT_IBAN)
                .balance(ACCOUNT_BALANCE)
                .build();
    }
}