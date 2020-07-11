package com.immccc.bank.transaction.status;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder(builderClassName = "TransactionStatusBuilder", toBuilder = true)
@JsonDeserialize(builder = TransactionStatus.TransactionStatusBuilder.class)
class TransactionStatus {
    @JsonPOJOBuilder(withPrefix = "")
    static class TransactionStatusBuilder {
    }

    String reference;
    Status status;
    BigDecimal amount;
    BigDecimal fee;

}
