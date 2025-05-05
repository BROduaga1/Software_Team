package com.example.gymcrm.service.impl;

import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.service.UsernameGeneratorService;
import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class UsernameGeneratorServiceImpl implements UsernameGeneratorService {
    private final UserRepository userRepository;

    @Override
    public String generateUsername(String firstName, String lastName) {
        if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
            throw new IllegalArgumentException("First name and last name must not be null");
        }

        String baseUsername = firstName + "." + lastName;
        String username = baseUsername;
        int counter = 1;

        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter++;
        }

        return username;
    }
}
