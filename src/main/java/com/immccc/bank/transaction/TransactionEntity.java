package com.immccc.bank.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class TransactionEntity {

    @Id
    private String reference;
    private String accountIban;

    @Column(name = "transaction_date")
    private ZonedDateTime date;
    private BigDecimal amount;
    private BigDecimal fee;
    private String description;
}
