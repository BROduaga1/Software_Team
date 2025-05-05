package com.example.gymcrm.util;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PasswordGenerator {

    @Value("${password.length}")
    @Setter
    private Integer passwordLength;

    public String generatePassword() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, passwordLength);
    }
}
