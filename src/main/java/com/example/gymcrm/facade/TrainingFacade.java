package com.example.gymcrm.facade;

import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.service.TrainingService;
import com.example.gymcrm.service.TrainingTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Component
@Validated
@RequiredArgsConstructor
public class TrainingFacade {
    private final TrainingService trainingService;
    private final TrainingTypeService trainingTypeService;

    public TrainingDto getTrainingById(Long id) {
        return trainingService.getById(id);
    }

    public TrainingDto createTraining(@Valid TrainingDto trainingDto) {
        return trainingService.create(trainingDto);
    }

    public Page<TrainingDto> findAllTrainingsByCriteria(@Valid TrainingSearchDto trainingSearchDto, Pageable pageable) {
        return trainingService.findAllByCriteria(trainingSearchDto, pageable);
    }

    public List<TrainingType> getAllTrainingTypes() {
        return trainingTypeService.getAll();
    }
}
