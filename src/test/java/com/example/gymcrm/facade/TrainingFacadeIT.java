package com.example.gymcrm.facade;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.example.gymcrm.TestData.createTrainingDto;
import static org.junit.jupiter.api.Assertions.*;

class TrainingFacadeIT extends BaseH2Test {

    @Autowired
    private TrainingFacade facade;

    @Autowired
    private UserFacade userFacade;

    private TrainingDto trainingDtoWithUsers;

    @BeforeEach
    void setUp() {
        UserLoginDto trainerLoginDto = userFacade.createTrainer(TestData.createTrainerCreateDto());
        UserLoginDto traineeLoginDto = userFacade.createTrainee(TestData.createTraineeCreateDto());
        trainingDtoWithUsers = createTrainingDto();
        trainingDtoWithUsers.setTraineeUsername(traineeLoginDto.getUsername());
        trainingDtoWithUsers.setTrainerUsername(trainerLoginDto.getUsername());
    }

    @Test
    void shouldCreateTraining() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        assertNotNull(createdTraining.getId());
    }

    @Test
    void shouldGetTrainingById() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        TrainingDto result = facade.getTrainingById(createdTraining.getId());
        assertEquals(createdTraining, result);
    }

    @Test
    void shouldFindAllTrainingsByCriteria() {
        TrainingDto createdTraining = facade.createTraining(trainingDtoWithUsers);
        TrainingSearchDto trainingSearchDto = new TrainingSearchDto();
        trainingSearchDto.setIsTrainee(true);
        trainingSearchDto.setUsername(createdTraining.getTraineeUsername());

        Page<TrainingDto> result = facade.findAllTrainingsByCriteria(trainingSearchDto, PageRequest.of(0, 10));
        assertTrue(result.getContent().contains(createdTraining));
    }

    @Test
    void shouldGetAllTrainingTypes() {
        List<TrainingType> result = facade.getAllTrainingTypes();
        assertFalse(result.isEmpty());
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTraining() {
        TrainingDto invalidTrainingDto = createTrainingDto();
        invalidTrainingDto.setName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTraining(invalidTrainingDto));
    }
}
