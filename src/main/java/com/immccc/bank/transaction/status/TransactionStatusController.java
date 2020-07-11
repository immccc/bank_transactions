package com.immccc.bank.transaction.status;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("transactions/status")
class TransactionStatusController {

    private final TransactionStatusService service;

    @GetMapping
    ResponseEntity<TransactionStatus> get(@RequestBody TransactionStatusQuery query) {
        TransactionStatus transactionStatus = service.get(query, ZonedDateTime.now());
        return ResponseEntity
                .status(transactionStatus.getStatus().getHttpStatus())
                .body(transactionStatus);
    }


}
