package com.example.gymcrm.monitoring.metrics;

import com.example.gymcrm.repository.UserRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserMetrics {

    private final MeterRegistry meterRegistry;
    private final UserRepository userRepository;

    @PostConstruct
    public void initMetrics() {
        Gauge.builder("users.active.count", userRepository, repo -> repo.countByIsActive(true))
                .description("Number of active users")
                .register(meterRegistry);

        Gauge.builder("users.total.count", userRepository, UserRepository::count)
                .description("Total number of users")
                .register(meterRegistry);
    }
}
