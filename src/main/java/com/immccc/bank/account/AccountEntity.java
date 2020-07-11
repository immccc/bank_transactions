package com.immccc.bank.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class AccountEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String iban;
    private BigDecimal balance;


}
