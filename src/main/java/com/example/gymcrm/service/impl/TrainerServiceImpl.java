package com.example.gymcrm.service.impl;

import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.service.TrainerService;
import com.example.gymcrm.service.UsernameGeneratorService;
import com.example.gymcrm.util.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService {
    private static final String TRAINER_NOT_FOUND = "Trainer wasn't found with username:";

    private final TrainerRepository trainerRepository;
    private final TrainerMapper trainerMapper;
    private final UsernameGeneratorService usernameGeneratorService;
    private final PasswordGenerator passwordGenerator;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserLoginDto create(TrainerCreateDto trainerDto) {
        Trainer trainer = trainerMapper.toEntity(trainerDto);
        String rawPassword = passwordGenerator.generatePassword();
        trainer.getUser().setIsActive(true);
        trainer.getUser().setPassword(passwordEncoder.encode(rawPassword));
        trainer.getUser().setUsername(usernameGeneratorService.generateUsername(
                trainer.getUser().getFirstName(),
                trainer.getUser().getLastName()));

        Trainer savedTrainer = trainerRepository.save(trainer);
        UserLoginDto userLoginDto = trainerMapper.toLoginDto(savedTrainer);
        userLoginDto.setPassword(rawPassword);
        return userLoginDto;
    }

    @Override
    @Transactional
    public TrainerDto getByUsername(String username) {
        return trainerMapper.toDto(getEntityByUsername(username));
    }

    @Override
    @Transactional
    public TrainerDto update(TrainerUpdateDto trainerDto) {
        Trainer trainer = getEntityByUsername(trainerDto.getUser().getUsername());

        trainerMapper.updateEntityFromDto(trainerDto, trainer);
        Trainer updatedTrainer = trainerRepository.save(trainer);
        LOGGER.info("Trainer with ID {} updated", updatedTrainer.getUser().getId());
        return trainerMapper.toDto(updatedTrainer);
    }

    @Override
    public boolean changeStatus(String username) {
        Trainer existingTrainer = getEntityByUsername(username);
        existingTrainer.getUser().setIsActive(!existingTrainer.getUser().getIsActive());
        trainerRepository.save(existingTrainer);
        return trainerRepository.save(existingTrainer).getUser().getIsActive();
    }

    @Override
    @Transactional
    public List<TrainerDto> getUnassignedTrainersListByUsername(String traineeUsername) {
        return trainerRepository.getUnassignedTrainersList(traineeUsername).stream()
                .map(trainerMapper::toDto)
                .toList();
    }

    private Trainer getEntityByUsername(String username) {
        return trainerRepository.findByUserUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(TRAINER_NOT_FOUND + username));
    }
}
