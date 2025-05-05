package com.example.gymcrm.repository;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.repository.specification.TrainingSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static com.example.gymcrm.TestData.createSearchDto;
import static org.junit.jupiter.api.Assertions.*;

class TrainingRepositoryImplTest extends BaseRepositoryTest {

    @Autowired
    private TrainingRepository trainingRepository;

    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private TraineeRepository traineeRepository;

    private Training training;

    @BeforeEach
    void setUp() {
        Trainer trainer = TestData.createTrainer();
        Trainee trainee = TestData.createTrainee();
        trainer.setId(null);
        trainee.setId(null);
        trainer.getUser().setId(null);
        trainee.getUser().setId(null);

        trainer = trainerRepository.save(trainer);
        trainee = traineeRepository.save(trainee);
        training = TestData.createTraining();
        training.setId(null);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
    }

    @Test
    void shouldReturnPresentTrainingById() {
        Training savedTraining = trainingRepository.save(training);
        Optional<Training> result = trainingRepository.findById(savedTraining.getId());
        assertTrue(result.isPresent());
        assertEquals(savedTraining, result.get());
    }

    @Test
    void shouldReturnEmptyWhenTrainingNotFoundById() {
        Optional<Training> result = trainingRepository.findById(1L);
        assertFalse(result.isPresent());
    }

    @Test
    void shouldSaveTrainingWithGeneratedIdWhenIdIsNull() {
        trainingRepository.save(training);
        assertNotNull(training.getId());
    }

    @Test
    void shouldSaveTrainingWithoutChangingIdWhenIdIsNotNull() {
        Long existingId = trainingRepository.save(training).getId();
        training.setId(existingId);
        trainingRepository.save(training);
        assertEquals(existingId, training.getId());
    }

    @ParameterizedTest
    @CsvSource({
            "true, true, true, true",
            "true, false, false, false",
            "false, true, true, true",
            "false, false, false, false"
    })
    void shouldFindByCriteria(boolean isTrainee, boolean expectUsername, boolean expectFromDate, boolean expectToDate) {
        Training savedTraining = trainingRepository.save(training);

        TrainingSearchDto searchDto = createSearchDto(savedTraining, isTrainee, expectUsername, expectFromDate, expectToDate);
        Pageable pageable = PageRequest.of(0, 10);
        Specification<Training> specification = TrainingSpecification.buildSpecification(searchDto);
        Page<Training> trainingPage = trainingRepository.findAll(specification, pageable);

        assertTrainingMatch(trainingPage, savedTraining, expectUsername, expectFromDate, expectToDate);
    }

    private void assertTrainingMatch(Page<Training> trainingPage, Training savedTraining,
                                     boolean expectUsername, boolean expectFromDate, boolean expectToDate) {
        List<Training> trainings = trainingPage.getContent();
        boolean shouldContain = expectUsername && expectFromDate && expectToDate;

        if (shouldContain) {
            assertTrue(trainings.contains(savedTraining));
        } else {
            assertFalse(trainings.contains(savedTraining));
        }
    }
}
