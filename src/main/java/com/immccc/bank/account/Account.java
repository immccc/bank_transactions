package com.immccc.bank.account;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;


@Value
@Builder(builderClassName = "AccountBuilder", toBuilder = true)
@JsonDeserialize(builder = Account.AccountBuilder.class)
class Account {

    @JsonPOJOBuilder(withPrefix = "")
    static class AccountBuilder {
    }

    @EqualsAndHashCode.Include
    String iban;

    @JsonProperty(defaultValue = "0.0")
    BigDecimal balance;

}
