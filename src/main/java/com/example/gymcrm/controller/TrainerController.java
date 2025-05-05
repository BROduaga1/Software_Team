package com.example.gymcrm.controller;

import com.example.gymcrm.controller.logging.LogTransaction;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.UserFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
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
@RequestMapping("/api/trainers")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Trainers", description = "Operations related to managing trainers")
public class TrainerController {

    private final UserFacade userFacade;

    @LogTransaction
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new trainer", description = "Creates a new trainer with the provided details.")
    public UserLoginDto createTrainer(@RequestBody @Valid TrainerCreateDto request) {
        return userFacade.createTrainer(request);
    }

    @LogTransaction
    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get trainer details", description = "Retrieves details of a trainer by username.")
    public TrainerDto selectTrainer(@PathVariable("username") String username) {
        return userFacade.getTrainerByUsername(username);
    }

    @LogTransaction
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update trainer details", description = "Updates details of a trainer by username.")
    public TrainerDto updateTrainer(
            @RequestBody @Valid TrainerUpdateDto request
    ) {
        return userFacade.updateTrainer(request);
    }

    @LogTransaction
    @GetMapping("/{username}/unassigned")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get unassigned trainers", description = "Retrieves a list of unassigned trainers for a trainee.")
    public List<TrainerDto> getUnassignedTrainers(
            @PathVariable("username") String username
    ) {
        return userFacade.getUnassignedTrainersListByUsername(username);
    }


    @LogTransaction
    @PatchMapping("/{username}/status")
    @ResponseStatus(HttpStatus.OK)
    public void changeStatus(
            @PathVariable("username") String username) {
        userFacade.changeTrainerStatus(username);
    }

}
