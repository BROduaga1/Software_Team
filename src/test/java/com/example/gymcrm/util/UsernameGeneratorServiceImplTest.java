package com.example.gymcrm.util;

import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.service.impl.UsernameGeneratorServiceImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsernameGeneratorServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UsernameGeneratorServiceImpl usernameGeneratorService;

    @ParameterizedTest
    @CsvSource({
            "mike,smith,false,mike.smith",
            "mike,smith,true,mike.smith1",
            "santa,claus,false,santa.claus",
    })
    void shouldGenerateExpectedUsername(String firstName, String lastName, Boolean exists, String expectedUsername) {
        when(userRepository.existsByUsername(firstName + "." + lastName)).thenReturn(exists);
        lenient().when(userRepository.existsByUsername(firstName + "." + lastName + "1")).thenReturn(false);

        String username = usernameGeneratorService.generateUsername(firstName, lastName);
        assertEquals(expectedUsername, username);
    }

    @ParameterizedTest
    @CsvSource({
            ",doe",
            "'',doe",
            "'',''",
            "john,"
    })
    void shouldThrowExceptionWhenFirstNameIsNull(String firstName, String lastName) {
        assertThrows(IllegalArgumentException.class, () -> usernameGeneratorService.generateUsername(firstName, lastName));
    }

}
