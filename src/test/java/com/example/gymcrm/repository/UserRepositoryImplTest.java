package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class UserRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = TestData.createTrainee().getUser();
        user.setId(null);
    }

    @Test
    void shouldReturnTrueWhenUserExistsByUsername() {
        userRepository.save(user);
        boolean exists = userRepository.existsByUsername(user.getUsername());
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseWhenUserDoesNotExistByUsername() {
        boolean exists = userRepository.existsByUsername("nonexistent");
        assertFalse(exists);
    }

    @Test
    void shouldReturnUserWhenFoundByUsername() {
        userRepository.save(user);
        Optional<User> result = userRepository.findByUsername(user.getUsername());
        assertTrue(result.isPresent());
        assertEquals(user.getLastName(), result.get().getLastName());
    }

    @Test
    void shouldReturnEmptyWhenUserNotFoundByUsername() {
        Optional<User> result = userRepository.findByUsername("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveUserWithGeneratedIdWhenIdIsNull() {
        user.setId(null);
        userRepository.save(user);
        assertNotNull(user.getId());
    }

    @Test
    void shouldSaveUserWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = userRepository.save(user).getId();
        user.setId(existingId);
        userRepository.save(user);
        assertEquals(existingId, user.getId());
    }
}
