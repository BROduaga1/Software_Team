package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.repository.TrainingTypeRepository;
import com.example.gymcrm.service.TrainingTypeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainingTypeServiceImpl implements TrainingTypeService {

    private final TrainingTypeRepository trainingTypeRepository;

    @Override
    public List<TrainingType> getAll() {
        return trainingTypeRepository.findAll();
    }
}
