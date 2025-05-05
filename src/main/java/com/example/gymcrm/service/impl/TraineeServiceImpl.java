package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.mapper.TraineeMapper;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TraineeRepository;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.service.TraineeService;
import com.example.gymcrm.service.UsernameGeneratorService;
import com.example.gymcrm.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService {
    private static final String TRAINEE_NOT_FOUND = "Trainee wasn't found with username:";

    private final TraineeRepository traineeRepository;
    private final TraineeMapper traineeMapper;
    private final UsernameGeneratorService usernameGeneratorService;
    private final PasswordGenerator passwordGenerator;
    private final TrainerMapper trainerMapper;
    private final TrainerRepository trainerRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserLoginDto create(TraineeCreateDto traineeDto) {
        Trainee trainee = traineeMapper.toEntity(traineeDto);
        String rawPassword = passwordGenerator.generatePassword();
        trainee.getUser().setIsActive(true);
        trainee.getUser().setPassword(passwordEncoder.encode(rawPassword));
        trainee.getUser().setUsername(usernameGeneratorService.generateUsername(
                trainee.getUser().getFirstName(),
                trainee.getUser().getLastName()));


        traineeRepository.save(trainee);
        LOGGER.info("Saved trainee with ID: {}", trainee.getId());

        UserLoginDto userLoginDto = traineeMapper.toLoginDto(trainee);
        userLoginDto.setPassword(rawPassword);
        return userLoginDto;
    }

    @Override
    @Transactional
    public TraineeDto getByUsername(String username) {
        return traineeMapper.toDto(getEntityByUsername(username));
    }

    @Override
    public TraineeDto update(TraineeUpdateDto traineeDto) {
        Trainee trainee = getEntityByUsername(traineeDto.getUser().getUsername());

        traineeMapper.updateEntityFromDto(traineeDto, trainee);
        Trainee updatedTrainee = traineeRepository.save(trainee);
        LOGGER.info("Trainee with ID {} updated", updatedTrainee.getUser().getId());
        return traineeMapper.toDto(updatedTrainee);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        getEntityByUsername(username);
        traineeRepository.deleteByUserUsername(username);
        LOGGER.info("Removed trainee with Id: {}", username);
    }

    @Override
    public boolean changeStatus(String username) {
        Trainee existingTrainee = getEntityByUsername(username);
        existingTrainee.getUser().setIsActive(!existingTrainee.getUser().getIsActive());
        traineeRepository.save(existingTrainee);
        return traineeRepository.save(existingTrainee).getUser().getIsActive();
    }

    @Override
    public List<TrainerShortInfoDto> updateTrainersListByUsername(String traineeUsername, List<String> trainerUsernames) {
        Trainee existingTrainee = getEntityByUsername(traineeUsername);
        List<Trainer> trainersList = new ArrayList<>(trainerUsernames.stream()
                .map(username -> trainerRepository.findByUserUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException("Trainer wasn't found with username:" + username)))
                .toList());
        existingTrainee.setTrainers(trainersList);
        traineeRepository.save(existingTrainee);
        return trainerMapper.toDtoList(trainersList);
    }

    private Trainee getEntityByUsername(String username) {
        LOGGER.info("Selecting trainee with id {}", username);
        return traineeRepository.findByUserUsername(username).orElseThrow(() ->
                new EntityNotFoundException(TRAINEE_NOT_FOUND + username)
        );
    }
}
