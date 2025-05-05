package com.example.gymcrm.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransactionIdGenerator {

    public String generateTransactionId() {
        return UUID.randomUUID().toString();
    }
}
