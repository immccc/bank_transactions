package com.immccc.bank.transaction.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static java.time.temporal.ChronoField.EPOCH_DAY;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@NoArgsConstructor
@AllArgsConstructor
@Getter
enum Status {
    SETTLED(-1, OK),
    PENDING(0, OK),
    FUTURE(1, OK),
    INVALID(null, NOT_FOUND);

    private Integer comparisonResult;
    private HttpStatus httpStatus;

    static Status from(ZonedDateTime transactionTime, ZonedDateTime  compareFromTime) {
        long compareFromDay = ZonedDateTime.from(compareFromTime).getLong(EPOCH_DAY);
        long transactionDay = transactionTime.getLong(EPOCH_DAY);

        int comparisonResult = Long.compare(transactionDay, compareFromDay);

        return Stream.of(Status.values())
                .filter(status -> status.getComparisonResult() == comparisonResult)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Cannot find status from comparison result " + comparisonResult));

    }
}
