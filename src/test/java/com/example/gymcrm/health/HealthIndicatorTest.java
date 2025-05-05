package com.example.gymcrm.health;

import com.example.gymcrm.monitoring.DatabaseHealthIndicator;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthIndicatorTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private DatabaseHealthIndicator healthIndicator;

    @Test
    void shouldReturnNegativeWhenEntityManagerIsClosed() {
        when(entityManager.isOpen()).thenReturn(false);

        Health health = healthIndicator.health();
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }

    @Test
    void shouldReturnNegativeWhenQueryFails() {
        when(entityManager.isOpen()).thenReturn(true);
        when(entityManager.createNativeQuery("SELECT 1")).thenThrow(new RuntimeException("DB error"));

        Health health = healthIndicator.health();
        assertThat(health.getStatus()).isEqualTo(Status.DOWN);
    }
}
