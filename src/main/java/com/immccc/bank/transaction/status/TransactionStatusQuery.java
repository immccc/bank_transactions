package com.immccc.bank.transaction.status;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "TransactionStatusQueryBuilder", toBuilder = true)
@JsonDeserialize(builder = TransactionStatusQuery.TransactionStatusQueryBuilder.class)
class TransactionStatusQuery {
    @JsonPOJOBuilder(withPrefix = "")
    static class TransactionStatusQueryBuilder {
    }

    @JsonProperty(required = true)
    String reference;
    Channel channel;
}


