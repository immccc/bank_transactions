package com.immccc.bank.transaction.status;

import com.immccc.bank.transaction.Transaction;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.immccc.bank.transaction.status.Channel.ATM;
import static com.immccc.bank.transaction.status.Channel.CLIENT;
import static com.immccc.bank.transaction.status.Channel.INTERNAL;

@Component
class TransactionStatusMapper {
    private final Map<Channel, BiFunction<Transaction, Status, TransactionStatus>> statusConverterByChannel;

    TransactionStatusMapper() {
        statusConverterByChannel = new EnumMap<>(Channel.class);

        statusConverterByChannel.put(ATM, this::getStatusWithAmountMinusFee);
        statusConverterByChannel.put(CLIENT, this::getStatusWithAmountMinusFee);
        statusConverterByChannel.put(INTERNAL, this::getStatusWithAmountAndFee);
    }


    public TransactionStatus from(Channel channel, Transaction transaction, Status status) {
        return statusConverterByChannel.get(channel).apply(transaction, status);
    }

    private TransactionStatus getStatusWithAmountAndFee(Transaction transaction, Status status) {
        return TransactionStatus.builder()
                .reference(transaction.getReference())
                .amount(transaction.getAmount())
                .fee(transaction.getFee())
                .status(status)
                .build();
    }

    private TransactionStatus getStatusWithAmountMinusFee(Transaction transaction, Status status) {
        return TransactionStatus.builder()
                .reference(transaction.getReference())
                .amount(transaction.getAmount().subtract(transaction.getFee()))
                .status(status)
                .build();
    }
}
