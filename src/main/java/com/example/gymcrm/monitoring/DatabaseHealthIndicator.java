package com.example.gymcrm.monitoring;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class DatabaseHealthIndicator implements HealthIndicator {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Health health() {
        long duration = checkDatabaseHealth();
        if (duration >= 0) {
            return Health.up().withDetail("db", "Available").withDetail("Response Time", duration + "ms").build();
        } else {
            return Health.down().withDetail("db", "Not Available").build();
        }
    }

    private long checkDatabaseHealth() {
        long startTime = System.currentTimeMillis();
        if (!entityManager.isOpen()) {
            return -1;
        }
        try {
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
        } catch (Exception e) {
            LOGGER.error("Database health check failed", e);
            return -1;
        }
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
