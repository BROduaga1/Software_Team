package com.example.gymcrm.facade;


import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.security.service.AuthService;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
@RequiredArgsConstructor
public class UserFacade {
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final AuthService authService;

    public TraineeDto getTraineeByUsername(String username) {
        return traineeService.getByUsername(username);
    }

    public UserLoginDto createTrainee(@Valid TraineeCreateDto traineeDto) {
        return traineeService.create(traineeDto);
    }

    public UserLoginDto createTrainer(@Valid TrainerCreateDto trainerDto) {
        return trainerService.create(trainerDto);
    }

    public TraineeDto updateTrainee(@Valid TraineeUpdateDto traineeDto) {
        return traineeService.update(traineeDto);
    }

    public TrainerDto updateTrainer(@Valid TrainerUpdateDto trainerDto) {
        return trainerService.update(trainerDto);
    }

    public TrainerDto getTrainerByUsername(String username) {
        return trainerService.getByUsername(username);
    }

    public boolean changeTrainerStatus(String username) {
        return trainerService.changeStatus(username);
    }

    public void deleteTraineeByUsername(String username) {
        traineeService.deleteByUsername(username);
    }

    public boolean changeTraineeStatus(String username) {
        return traineeService.changeStatus(username);
    }

    public List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername) {
        return trainerService.getUnassignedTrainersListByUsername(traineeUsername);
    }

    public List<TrainerShortInfoDto> updateTrainersListByUsername(String traineeUsername, @Valid List<String> trainers) {
        return traineeService.updateTrainersListByUsername(traineeUsername, trainers);
    }

    public void changePassword(ChangeLoginDto loginDto) {
        authService.changePassword(loginDto);
    }

}
