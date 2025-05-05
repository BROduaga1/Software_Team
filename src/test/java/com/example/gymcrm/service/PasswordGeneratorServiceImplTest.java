package com.example.gymcrm.service;

import com.example.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class PasswordGeneratorServiceImplTest {

    private static final Integer passwordLength = 10;

    private PasswordGenerator passwordGenerator;

    @BeforeEach
    void setUp() {
        passwordGenerator = new PasswordGenerator();
        passwordGenerator.setPasswordLength(passwordLength);
    }

    @Test
    void shouldGeneratePasswordOfCorrectLength() {
        assertThat(passwordGenerator.generatePassword()).hasSize(passwordLength);
    }

    @Test
    void shouldGenerateNonEmptyPassword() {
        assertThat(passwordGenerator.generatePassword()).isNotEmpty();
    }

    @Test
    void shouldGenerateUniquePasswords() {
        assertThat(passwordGenerator.generatePassword()).isNotEqualTo(passwordGenerator.generatePassword());
    }
}
