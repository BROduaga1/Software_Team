package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingMapperTest {

    @Mock
    private TrainerMapper trainerMapper;

    @Mock
    private TraineeMapper traineeMapper;

    @InjectMocks
    private TrainingMapperImpl mapper;

    @Test
    void shouldMapTrainingToTrainingDto() {
        Training training = TestData.createTraining();
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(training.getId()).isEqualTo(trainingDto.getId());
        assertThat(training.getName()).isEqualTo(trainingDto.getName());
        assertThat(training.getTrainee().getUser().getUsername()).isEqualTo(trainingDto.getTraineeUsername());
        assertThat(training.getTrainer().getUser().getUsername()).isEqualTo(trainingDto.getTrainerUsername());
    }

    @Test
    void shouldMapTrainingDtoToTraining() {
        TrainingDto trainingDto = TestData.createTrainingDto();
        Training training = mapper.toEntity(trainingDto);
        assertThat(trainingDto.getId()).isEqualTo(training.getId());
        assertThat(trainingDto.getName()).isEqualTo(training.getName());
        assertThat(trainingDto.getTraineeUsername()).isEqualTo(training.getTrainee().getUser().getUsername());
        assertThat(trainingDto.getTrainerUsername()).isEqualTo(training.getTrainer().getUser().getUsername());
    }

    @Test
    void shouldHandleNullTrainingToTrainingDto() {
        TrainingDto trainingDto = mapper.toDto(null);
        assertThat(trainingDto).isNull();
    }

    @Test
    void shouldHandleNullTrainingDtoToTraining() {
        Training training = mapper.toEntity(null);
        assertThat(training).isNull();
    }

    @Test
    void shouldMapTrainingsListToTrainingDtoList() {
        Training training = TestData.createTraining();
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(mapper.toDtoList(List.of(training))).containsExactlyInAnyOrder(trainingDto);
    }

    @Test
    void shouldHandleNullTrainingsListToTrainingDtoList() {
        List<TrainingDto> trainingDtos = mapper.toDtoList(null);
        assertThat(trainingDtos).isNull();
    }

    @Test
    void shouldMapEmptyTrainingsListToTrainingDtoList() {
        List<TrainingDto> trainingDtos = mapper.toDtoList(List.of());
        assertThat(trainingDtos).isEmpty();
    }

    @Test
    void shouldReturnNullTraineeUsernameWhenTraineeIsNull() {
        Training training = TestData.createTraining();
        training.setTrainee(null);
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(trainingDto.getTraineeUsername()).isNull();
    }

    @Test
    void shouldReturnNullTraineeUsernameWhenTraineeUserIsNull() {
        Training training = TestData.createTraining();
        training.getTrainee().setUser(null);
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(trainingDto.getTraineeUsername()).isNull();
    }

    @Test
    void shouldReturnNullTrainerUsernameWhenTrainerIsNull() {
        Training training = TestData.createTraining();
        training.setTrainer(null);
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(trainingDto.getTrainerUsername()).isNull();
    }

    @Test
    void shouldReturnNullTrainerUsernameWhenTrainerUserIsNull() {
        Training training = TestData.createTraining();
        training.getTrainer().setUser(null);
        TrainingDto trainingDto = mapper.toDto(training);
        assertThat(trainingDto.getTrainerUsername()).isNull();
    }

}
