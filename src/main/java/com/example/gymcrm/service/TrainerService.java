package com.example.gymcrm.service;

import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;

import java.util.List;

public interface TrainerService {

    /**
     * Retrieves a TrainerDto by username.
     *
     * @param username the username of the trainer
     * @return the TrainerDto
     */
    TrainerDto getByUsername(String username) throws EntityNotFoundException;

    /**
     * Creates a new Trainer.
     *
     * @param trainerDto the TrainerDto containing the trainer details
     * @return the created TrainerDto
     */
    UserLoginDto create(TrainerCreateDto trainerDto);

    /**
     * Updates an existing Trainer.
     *
     * @param trainerDto the TrainerDto containing the updated trainer details
     * @return the updated TrainerDto
     */
    TrainerDto update(TrainerUpdateDto trainerDto) throws EntityNotFoundException;

    /**
     * Changes the status of a Trainer.
     *
     * @param username the username of the trainer
     * @return the new status of the trainer
     */
    boolean changeStatus(String username) throws EntityNotFoundException;

    /**
     * Retrieves a list of unassigned trainers by trainee username.
     *
     * @param traineeUsername the username of the trainee
     * @return the list of unassigned TrainerDto
     */
    List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername);
}
