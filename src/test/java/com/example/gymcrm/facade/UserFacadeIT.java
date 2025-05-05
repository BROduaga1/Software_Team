package com.example.gymcrm.facade;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.UserRepository;
import com.example.gymcrm.security.service.AuthService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;

import static com.example.gymcrm.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class UserFacadeIT extends BaseH2Test {

    @Autowired
    AuthService authService;
    @Autowired
    private UserFacade facade;
    @Autowired
    private TraineeRepository traineeRepository;
    @Autowired
    private TrainerRepository trainerRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserLoginDto trainerLoginDto = facade.createTrainer(createTrainerCreateDto());
        UserLoginDto traineeLoginDto = facade.createTrainee(createTraineeCreateDto());
        TrainingDto trainingDtoWithUsers = createTrainingDto();
        trainingDtoWithUsers.setTraineeUsername(traineeLoginDto.getUsername());
        trainingDtoWithUsers.setTrainerUsername(trainerLoginDto.getUsername());
    }

    @Test
    void shouldCreateAndGetTrainee() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        TraineeDto result = facade.getTraineeByUsername(createdTraineeLogin.getUsername());
        assertEquals(traineeCreateDto.getUser().getLastName(), result.getUser().getLastName());
    }

    @Test
    void shouldUpdateTrainee() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        facade.getTraineeByUsername(createdTraineeLogin.getUsername());
        TraineeUpdateDto updateDto = TestData.createTraineeUpdateDto();
        updateDto.getUser().setUsername(createdTraineeLogin.getUsername());
        updateDto.getUser().setFirstName("Updated");
        updateDto.setAddress("Updated");
        facade.updateTrainee(updateDto);
        TraineeDto result = facade.getTraineeByUsername(createdTraineeLogin.getUsername());
        assertEquals("Updated", result.getUser().getFirstName());
        assertEquals("Updated", result.getAddress());
    }

    @Test
    void shouldDeleteTrainee() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        facade.deleteTraineeByUsername(createdTraineeLogin.getUsername());
        String username = createdTraineeLogin.getUsername();
        assertThrows(EntityNotFoundException.class, () -> facade.getTraineeByUsername(username), TRAINEE_NOT_FOUND + createdTraineeLogin.getUsername());
    }

    @Test
    void shouldChangeTraineePassword() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        Trainee trainee = traineeRepository.findByUserUsername(createdTraineeLogin.getUsername()).orElseThrow();
        String oldPassword = trainee.getUser().getPassword();
        ChangeLoginDto changeLoginDto = new ChangeLoginDto(createdTraineeLogin.getUsername(), createdTraineeLogin.getPassword(), "newPassword");
        facade.changePassword(changeLoginDto);
        trainee = traineeRepository.findByUserUsername(createdTraineeLogin.getUsername()).orElseThrow();
        assertNotEquals(oldPassword, trainee.getUser().getPassword());
        assertNotNull(trainee.getUser().getPassword());
    }

    @Test
    void shouldChangeTraineeStatus() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        boolean initialStatus = facade.getTraineeByUsername(createdTraineeLogin.getUsername()).getUser().getIsActive();
        boolean newStatus = facade.changeTraineeStatus(createdTraineeLogin.getUsername());
        assertNotEquals(initialStatus, newStatus);
        boolean finalStatus = facade.changeTraineeStatus(createdTraineeLogin.getUsername());
        assertNotEquals(newStatus, finalStatus);
    }

    @Test
    void shouldGetUnassignedTrainersListByUsername() {
        TrainerCreateDto trainerCreateDto = createTrainerCreateDto();
        TraineeCreateDto traineeCreateDto = createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        UserLoginDto createdTrainerLogin = facade.createTrainer(trainerCreateDto);
        List<TrainerDto> result = facade.getUnassignedTrainersListByUsername(createdTraineeLogin.getUsername());
        assertTrue(result.stream().anyMatch(trainer -> trainer.getUser().getUsername().equals(createdTrainerLogin.getUsername())));
    }

    @Test
    void shouldUpdateTrainersListByUsername() {
        TrainerCreateDto trainerCreateDto = createTrainerCreateDto();
        TraineeCreateDto traineeCreateDto = createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        UserLoginDto createdTrainerLogin = facade.createTrainer(trainerCreateDto);
        List<String> trainerUsernames = Collections.singletonList(createdTrainerLogin.getUsername());
        List<TrainerShortInfoDto> result = facade.updateTrainersListByUsername(createdTraineeLogin.getUsername(), trainerUsernames);
        assertEquals(1, result.size());
        assertEquals(createdTrainerLogin.getUsername(), result.get(0).getUser().getUsername());
    }

    @Test
    void shouldUpdateTrainer() {
        TrainerCreateDto trainerCreateDto = createTrainerCreateDto();
        UserLoginDto createdTrainerLogin = facade.createTrainer(trainerCreateDto);
        facade.getTrainerByUsername(createdTrainerLogin.getUsername());
        TrainerUpdateDto updateDto = TestData.createTrainerUpdateDto();
        updateDto.getUser().setFirstName("Updated");
        updateDto.getUser().setUsername(createdTrainerLogin.getUsername());
        facade.updateTrainer(updateDto);
        TrainerDto result = facade.getTrainerByUsername(createdTrainerLogin.getUsername());
        assertEquals("Updated", result.getUser().getFirstName());
    }

    @Test
    void shouldChangeTrainerStatus() {
        TrainerCreateDto trainerCreateDto = TestData.createTrainerCreateDto();
        UserLoginDto createdTrainerLogin = facade.createTrainer(trainerCreateDto);
        boolean initialStatus = facade.getTrainerByUsername(createdTrainerLogin.getUsername()).getUser().getIsActive();
        boolean newStatus = facade.changeTrainerStatus(createdTrainerLogin.getUsername());
        assertNotEquals(initialStatus, newStatus);
        boolean finalStatus = facade.changeTrainerStatus(createdTrainerLogin.getUsername());
        assertNotEquals(newStatus, finalStatus);
    }

    @Test
    void shouldChangeTrainerPassword() {
        TrainerCreateDto trainerCreateDto = TestData.createTrainerCreateDto();
        UserLoginDto createdTrainerLogin = facade.createTrainer(trainerCreateDto);
        Trainer trainer = trainerRepository.findByUserUsername(createdTrainerLogin.getUsername()).orElseThrow();
        String oldPassword = trainer.getUser().getPassword();
        ChangeLoginDto changeLoginDto = new ChangeLoginDto(createdTrainerLogin.getUsername(), createdTrainerLogin.getPassword(), "newPassword");
        facade.changePassword(changeLoginDto);
        trainer = trainerRepository.findByUserUsername(createdTrainerLogin.getUsername()).orElseThrow();
        assertNotEquals(oldPassword, trainer.getUser().getPassword());
        assertNotNull(trainer.getUser().getPassword());
    }

    @Test
    void shouldNotChangePasswordWithIncorrectOldPassword() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        UserLoginDto createdTraineeLogin = facade.createTrainee(traineeCreateDto);
        ChangeLoginDto changeLoginDto = new ChangeLoginDto(createdTraineeLogin.getUsername(), "wrongOldPassword", "newPassword");
        assertThrows(IllegalArgumentException.class, () -> facade.changePassword(changeLoginDto));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        ChangeLoginDto changeLoginDto = new ChangeLoginDto("nonExistentUser", "oldPassword", "newPassword");
        assertThrows(EntityNotFoundException.class, () -> facade.changePassword(changeLoginDto));
    }

    @Test
    void shouldThrowErrorWhenDeletingNonExistentTrainee() {
        assertThrows(EntityNotFoundException.class, () -> facade.deleteTraineeByUsername("nonexistent"));
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTrainee() {
        TraineeCreateDto invalidTraineeCreateDto = createTraineeCreateDto();
        invalidTraineeCreateDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTrainee(invalidTraineeCreateDto));
    }

    @Test
    void shouldThrowErrorWhenCreatingInvalidTrainer() {
        TrainerCreateDto invalidTrainerCreateDto = createTrainerCreateDto();
        invalidTrainerCreateDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.createTrainer(invalidTrainerCreateDto));
    }

    @Test
    void shouldThrowErrorWhenUpdatingInvalidTrainee() {
        TraineeUpdateDto invalidTraineeUpdateDto = createTraineeUpdateDto();
        invalidTraineeUpdateDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.updateTrainee(invalidTraineeUpdateDto));
    }

    @Test
    void shouldThrowErrorWhenUpdatingInvalidTrainer() {
        TrainerUpdateDto invalidTrainerUpdateDto = createTrainerUpdateDto();
        invalidTrainerUpdateDto.getUser().setFirstName("");
        assertThrows(ConstraintViolationException.class, () -> facade.updateTrainer(invalidTrainerUpdateDto));
    }

}
