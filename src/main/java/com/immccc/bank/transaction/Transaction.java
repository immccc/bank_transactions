package com.immccc.bank.transaction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Value
@Builder(builderClassName = "TransactionBuilder", toBuilder = true)
@JsonDeserialize(builder = Transaction.TransactionBuilder.class)
public class Transaction {

    @JsonPOJOBuilder(withPrefix = "")
    public static class TransactionBuilder {
    }

    @EqualsAndHashCode.Include
    String reference;

    @EqualsAndHashCode.Include
    @JsonProperty(required = true)
    String accountIban;
    ZonedDateTime date;
    @JsonProperty(required = true)
    BigDecimal amount;
    @JsonProperty(required = true)
    BigDecimal fee;
    String description;
}
