package com.example.gymcrm.security.details;

import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.brute_force.LoginAttemptService;
import com.example.gymcrm.security.exception.TooManyRequestsAuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final LoginAttemptService rateLimiterService;

    @Value("${login.attempt.expire.minutes}")
    private int expirationTimeInMinutes;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (rateLimiterService.isBlocked()) {
            throw new TooManyRequestsAuthException("Too many failed attempts. Try again in " + expirationTimeInMinutes + " minutes");
        }

        if (!userRepository.isAdminByUsername(username)) {
            LOGGER.info("admin does not exist with username: {}", username);
            throw new BadCredentialsException("Bad credentials");
        }

        return userRepository.findByUsername(username)
                .map(UserDetailsImpl::new)
                .orElseThrow(() -> new BadCredentialsException("Bad credentials"));
    }
}
