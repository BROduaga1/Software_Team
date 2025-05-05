package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TraineeRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private TraineeRepository traineeRepository;

    private Trainee trainee;

    @BeforeEach
    void setUp() {
        trainee = TestData.createTrainee();
        trainee.setId(null);
        trainee.getUser().setId(null);
    }

    @Test
    @Transactional
    void shouldReturnPresentTraineeByUsername() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        Optional<Trainee> result = traineeRepository.findByUserUsername(savedTrainee.getUser().getUsername());
        assertTrue(result.isPresent());
        assertEquals(savedTrainee, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTraineeNotFoundByUsername() {
        Optional<Trainee> result = traineeRepository.findByUserUsername("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        traineeRepository.deleteByUserUsername(savedTrainee.getUser().getUsername());
        Optional<Trainee> result = traineeRepository.findByUserUsername(savedTrainee.getUser().getUsername());
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTraineeWithGeneratedIdWhenIdIsNull() {
        traineeRepository.save(trainee);
        assertNotNull(trainee.getId());
    }

    @Test
    void shouldSaveTraineeWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = traineeRepository.save(trainee).getId();
        trainee.setId(existingId);
        traineeRepository.save(trainee);
        assertEquals(existingId, trainee.getId());
    }

    @Test
    void shouldChangeStatus() {
        Trainee savedTrainee = traineeRepository.save(trainee);
        String username = savedTrainee.getUser().getUsername();
        boolean initialStatus = savedTrainee.getUser().getIsActive();
        savedTrainee.getUser().setIsActive(!initialStatus);
        traineeRepository.save(savedTrainee);
        boolean newStatus = traineeRepository.findByUserUsername(username).orElseThrow().getUser().getIsActive();
        assertNotEquals(initialStatus, newStatus);

        savedTrainee.getUser().setIsActive(!newStatus);
        traineeRepository.save(savedTrainee);
        boolean finalStatus = traineeRepository.findByUserUsername(username).orElseThrow().getUser().getIsActive();
        assertNotEquals(newStatus, finalStatus);
    }

}
