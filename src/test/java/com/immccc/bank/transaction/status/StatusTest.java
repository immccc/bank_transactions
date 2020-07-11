package com.immccc.bank.transaction.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.ZonedDateTime;
import java.util.stream.Stream;

import static com.immccc.bank.transaction.status.Status.FUTURE;
import static com.immccc.bank.transaction.status.Status.PENDING;
import static com.immccc.bank.transaction.status.Status.SETTLED;
import static java.time.ZoneId.systemDefault;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class StatusTest {

    static Stream<Arguments> fromTestParametersProvider() {
        return Stream.of(
                arguments(1, 2, SETTLED),
                arguments(2, 2, PENDING),
                arguments(2, 1, FUTURE)
        );
    }

    @DisplayName("Should get status according to when transaction was created")
    @ParameterizedTest(name = "when transaction was created at day of month {0} and query is done at {1}, status is {2}")
    @MethodSource("fromTestParametersProvider")
    void from(int dayOfMonthFromTransaction, int dayOfMonthFromTimeToCompare, Status expectedStatus) {
        Status actualStatus = Status.from(
                ZonedDateTime.of(2020, 1, dayOfMonthFromTransaction, 1, 1, 1, 1, systemDefault()),
                ZonedDateTime.of(2020, 1, dayOfMonthFromTimeToCompare, 1, 1, 1, 1, systemDefault()));

        assertThat(actualStatus).isEqualTo(expectedStatus);
    }

}