package com.example.gymcrm.service;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.mapper.TrainingMapper;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.TrainingRepository;
import com.example.gymcrm.service.impl.TrainingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.gymcrm.TestData.TRAINING_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {

    @InjectMocks
    private TrainingServiceImpl trainingService;
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingMapper trainingMapper;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeRepository traineeRepository;
    private TrainingDto trainingDto;
    private Training training;

    @BeforeEach
    void setUp() {
        trainingDto = TestData.createTrainingDto();
        training = TestData.createTraining();
    }

    @Test
    void shouldCreateTrainingSuccessfully() {
        when(trainingMapper.toEntity(trainingDto)).thenReturn(training);
        when(trainerRepository.findByUserUsername(trainingDto.getTrainerUsername())).thenReturn(Optional.of(training.getTrainer()));
        when(traineeRepository.findByUserUsername(trainingDto.getTraineeUsername())).thenReturn(Optional.of(training.getTrainee()));
        trainingService.create(trainingDto);
        verify(trainingRepository).save(training);
        verify(trainingMapper).toEntity(trainingDto);
        verify(trainingMapper).toDto(training);
        verifyNoMoreInteractions(trainingRepository, trainingMapper);
    }

    @Test
    void shouldThrowExceptionWhenCreatingNullTraining() {
        assertThrows(NullPointerException.class, () -> trainingService.create(null));
    }

    @Test
    void shouldRetrieveTrainingByIdSuccessfully() {
        when(trainingRepository.findById(trainingDto.getId())).thenReturn(Optional.of(training));
        when(trainingMapper.toDto(training)).thenReturn(trainingDto);
        TrainingDto retrieved = trainingService.getById(trainingDto.getId());

        assertNotNull(retrieved);
        assertEquals(trainingDto.getId(), retrieved.getId());
        verify(trainingRepository, times(1)).findById(trainingDto.getId());
    }

    @Test
    void shouldThrowExceptionWhenTrainingNotFoundById() {
        when(trainingRepository.findById(trainingDto.getId())).thenReturn(Optional.empty());
        Long id = trainingDto.getId();
        assertThrows(EntityNotFoundException.class, () -> trainingService.getById(id), TRAINING_NOT_FOUND + id);
    }
}
