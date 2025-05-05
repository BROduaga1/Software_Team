package com.example.gymcrm.monitoring;

import com.example.gymcrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserHealthIndicator implements HealthIndicator {

    private final UserRepository userRepository;

    @Override
    public Health health() {
        try {
            long activeUsers = userRepository.countByIsActive(true);
            return Health.up().withDetail("Active Users", activeUsers).build();
        } catch (Exception e) {
            LOGGER.error("Health check failed for UserRepository", e);
            return Health.down().withDetail("Error", e.getMessage()).build();
        }
    }
}
