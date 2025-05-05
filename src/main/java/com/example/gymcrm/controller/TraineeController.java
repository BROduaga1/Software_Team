package com.example.gymcrm.controller;

import com.example.gymcrm.controller.logging.LogTransaction;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.UserFacade;
import com.jcabi.aspects.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainees")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Trainees", description = "Operations related to managing trainees")
public class TraineeController {

    private final UserFacade userFacade;

    @LogTransaction
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new trainee", description = "Creates a new trainee with the provided details.")
    public UserLoginDto createTrainee(@RequestBody @Valid TraineeCreateDto request) {
        return userFacade.createTrainee(request);
    }

    @LogTransaction
    @Loggable
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get trainee details", description = "Retrieves details of a trainee by username.")
    public TraineeDto selectTrainee(@PathVariable("username") String username) {
        return userFacade.getTraineeByUsername(username);
    }

    @LogTransaction
    @PutMapping(value = "/")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainee details", description = "Updates details of a trainee by username.")
    public TraineeDto updateTrainee(
            @RequestBody @Valid TraineeUpdateDto request
    ) {
        return userFacade.updateTrainee(request);
    }

    @LogTransaction
    @DeleteMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete trainee", description = "Deletes a trainee by username.")
    public void deleteTrainee(@PathVariable("username") String username) {
        userFacade.deleteTraineeByUsername(username);
    }

    @LogTransaction
    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(
            @PathVariable("username") String username) {
        userFacade.changeTraineeStatus(username);
    }

    @LogTransaction
    @PutMapping(value = "/{username}/trainers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainers list", description = "Updates the list of trainers for a trainee.")
    public List<TrainerShortInfoDto> updateTrainersList(
            @PathVariable("username") String username,
            @RequestBody @Valid List<String> trainerUsernames
    ) {
        return userFacade.updateTrainersListByUsername(username, trainerUsernames);
    }

    @LogTransaction
    @Loggable
    @GetMapping("/{username}/trainers")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get available trainers", description = "Retrieves a list of available trainers for a trainee.")
    public List<TrainerDto> getAvailableTrainers(@PathVariable("username") String username) {
        return userFacade.getUnassignedTrainersListByUsername(username);
    }
}
