package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.mapper.TrainingMapper;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.repository.TrainingRepository;
import com.example.gymcrm.repository.specification.TrainingSpecification;
import com.example.gymcrm.service.TrainingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService {
    private static final String TRAINING_NOT_FOUND = "Training wasn't found with id:";

    private final TrainingRepository trainingRepository;
    private final TrainingMapper trainingMapper;

    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;

    @Override
    public TrainingDto getById(Long id) {
        LOGGER.info("Selecting training with id {}", id);
        return trainingRepository.findById(id).map(trainingMapper::toDto).orElseThrow(() ->
                new EntityNotFoundException(TRAINING_NOT_FOUND + id)
        );
    }

    @Override
    public TrainingDto create(TrainingDto trainingDto) {
        Training training = trainingMapper.toEntity(trainingDto);
        training.setId(null);

        Trainer trainer = trainerRepository.findByUserUsername(trainingDto.getTrainerUsername()).orElseThrow(
                () -> new EntityNotFoundException("Trainer wasn't found with username:" + trainingDto.getTrainerUsername())
        );
        Trainee trainee = traineeRepository.findByUserUsername(trainingDto.getTraineeUsername()).orElseThrow(() ->
                new EntityNotFoundException("Trainee wasn't found with username:" + trainingDto.getTraineeUsername())
        );

        training.setTrainer(trainer);
        training.setTrainee(trainee);

        trainingRepository.save(training);
        LOGGER.info("Saved training with ID: {}", training.getId());
        return trainingMapper.toDto(training);
    }

    @Override
    public Page<TrainingDto> findAllByCriteria(TrainingSearchDto trainingSearchDto, Pageable pageable) {
        LOGGER.info("Selecting trainings by criteria: {}", trainingSearchDto);
        Specification<Training> specification = TrainingSpecification.buildSpecification(trainingSearchDto);
        return trainingRepository.findAll(specification, pageable).map(trainingMapper::toDto);
    }

}
