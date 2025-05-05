package com.example.gymcrm.controller;

import com.example.gymcrm.controller.logging.LogTransaction;
import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.facade.TrainingFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/trainings")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Trainings", description = "Operations related to managing trainings")
public class TrainingController {

    private final TrainingFacade trainingFacade;

    @LogTransaction
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new training", description = "Creates a new training with the provided details.")
    public TrainingDto createTraining(@RequestBody @Valid TrainingDto request) {
        return trainingFacade.createTraining(request);
    }

    @LogTransaction
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get training details", description = "Retrieves details of a training by ID.")
    public TrainingDto getTrainingById(@PathVariable("id") Long id) {
        return trainingFacade.getTrainingById(id);
    }

    @LogTransaction
    @PostMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Search trainings", description = "Searches for trainings based on criteria.")
    public Page<TrainingDto> searchTrainings(@PageableDefault Pageable pageable, @RequestBody @Valid TrainingSearchDto searchDto) {
        return trainingFacade.findAllTrainingsByCriteria(searchDto, pageable);
    }


    @LogTransaction
    @GetMapping("/types")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all training types", description = "Retrieves a list of all training types.")
    public List<TrainingType> getAllTrainingTypes() {
        return trainingFacade.getAllTrainingTypes();
    }
}
