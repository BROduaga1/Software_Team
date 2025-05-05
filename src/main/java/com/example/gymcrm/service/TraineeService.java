package com.example.gymcrm.service;

import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;

import java.util.List;


public interface TraineeService {

    /**
     * Retrieves a trainee by their username.
     *
     * @param username the username of the trainee
     * @return the trainee DTO
     * @throws EntityNotFoundException if no trainee is found with the given username
     */
    TraineeDto getByUsername(String username) throws EntityNotFoundException;

    /**
     * Creates a new trainee.
     *
     * @param traineeDto the trainee DTO
     * @return the created trainee DTO
     * @throws IllegalArgumentException if the trainee data is invalid
     */
    UserLoginDto create(TraineeCreateDto traineeDto) throws IllegalArgumentException;

    /**
     * Updates an existing trainee.
     *
     * @param traineeDto the trainee DTO
     * @return the updated trainee DTO
     * @throws EntityNotFoundException if no trainee is found with the given username
     */
    TraineeDto update(TraineeUpdateDto traineeDto) throws EntityNotFoundException;

    /**
     * Deletes a trainee by their username.
     *
     * @param username the username of the trainee
     * @throws EntityNotFoundException if no trainee is found with the given username
     */
    void deleteByUsername(String username) throws EntityNotFoundException;

    /**
     * Changes the status of a trainee.
     *
     * @param username the username of the trainee
     * @return the new status of the trainee
     * @throws EntityNotFoundException if no trainee is found with the given username
     */
    boolean changeStatus(String username) throws EntityNotFoundException;


    /**
     * Retrieves a list of unassigned trainers for a trainee.
     *
     * @param traineeUsername the username of the trainee
     * @return the list of unassigned trainers
     * @throws EntityNotFoundException if no trainee is found with the given username
     */
    List<TrainerShortInfoDto> updateTrainersListByUsername(String traineeUsername, List<String> trainerUsernames);
}
