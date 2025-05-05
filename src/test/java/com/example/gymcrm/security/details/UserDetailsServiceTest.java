package com.example.gymcrm.security.details;

import com.example.gymcrm.domain.User;
import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.brute_force.LoginAttemptService;
import com.example.gymcrm.security.exception.TooManyRequestsAuthException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LoginAttemptService loginAttemptService;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void testLoadUserByUsername_UserFound() {
        String username = "testUser";
        User user = User.builder().username(username).build();

        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(userRepository.isAdminByUsername(username)).thenReturn(true);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());

        verify(loginAttemptService).isBlocked();
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String username = "nonExistentUser";

        when(loginAttemptService.isBlocked()).thenReturn(false);
        when(userRepository.isAdminByUsername(username)).thenReturn(true);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () ->
                userDetailsService.loadUserByUsername(username));

        assertEquals("Bad credentials", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_Blocked() {
        String username = "blockedUser";

        when(loginAttemptService.isBlocked()).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userDetailsService.loadUserByUsername(username));

        assertEquals(TooManyRequestsAuthException.class, exception.getClass());

        verify(loginAttemptService).isBlocked();
        verifyNoInteractions(userRepository);
    }
}

