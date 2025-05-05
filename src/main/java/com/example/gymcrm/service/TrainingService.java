package com.example.gymcrm.service;

import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TrainingService {

    /**
     * Retrieves a training by its ID.
     *
     * @param id the ID of the training
     * @return the TrainingDto
     */
    TrainingDto getById(Long id) throws EntityNotFoundException;

    /**
     * Creates a new training.
     *
     * @param trainingDto the TrainingDto containing the training details
     * @return the created TrainingDto
     */
    TrainingDto create(TrainingDto trainingDto) throws EntityNotFoundException;

    /**
     * Finds all trainings that match the given criteria.
     *
     * @param trainingSearchDto the criteria for searching trainings
     * @return the list of TrainingDto that match the criteria
     */
    Page<TrainingDto> findAllByCriteria(TrainingSearchDto trainingSearchDto, Pageable pageable);
}
