package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TrainerRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private TrainerRepository trainerRepository;

    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainer = TestData.createTrainer();
        trainer.setId(null);
        trainer.getUser().setId(null);
    }

    @Test
    void shouldReturnPresentTrainerByUsername() {
        Trainer savedTrainer = trainerRepository.save(trainer);
        Optional<Trainer> result = trainerRepository.findByUserUsername(savedTrainer.getUser().getUsername());
        assertTrue(result.isPresent());
        assertEquals(savedTrainer, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTrainerNotFoundByUsername() {
        Optional<Trainer> result = trainerRepository.findByUserUsername("nonexistent");
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTrainerWithGeneratedIdWhenIdIsNull() {
        trainerRepository.save(trainer);
        assertNotNull(trainer.getId());
    }

    @Test
    void shouldSaveTrainerWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = trainerRepository.save(trainer).getId();
        trainer.setId(existingId);
        trainerRepository.save(trainer);
        assertEquals(existingId, trainer.getId());
    }

    @Test
    void shouldFindAllTrainers() {
        Trainer savedTrainer1 = trainerRepository.save(trainer);
        Trainer savedTrainer2 = TestData.createTrainer();
        savedTrainer2.setId(null);
        savedTrainer2.getUser().setId(null);
        savedTrainer2.getUser().setUsername("another");
        trainerRepository.save(savedTrainer2);
        List<Trainer> trainers = trainerRepository.findAll();
        assertTrue(trainers.contains(savedTrainer1));
        assertTrue(trainers.contains(savedTrainer2));
    }
}
