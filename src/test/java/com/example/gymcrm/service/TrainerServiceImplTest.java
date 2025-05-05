package com.example.gymcrm.service;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.exception.EntityNotFoundException;
import com.example.gymcrm.mapper.TrainerMapper;
import com.example.gymcrm.repository.TrainerRepository;
import com.example.gymcrm.service.impl.TrainerServiceImpl;
import com.example.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {

    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private TrainerServiceImpl trainerService;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private TrainerMapper trainerMapper;
    @Mock
    private UsernameGeneratorService usernameGeneratorService;
    private TrainerDto trainerDto;
    private Trainer trainer;

    @BeforeEach
    void setUp() {
        trainerDto = TestData.createTrainerDto();
        trainer = TestData.createTrainer();
    }

    @Test
    void shouldCreateTrainerSuccessfully() {
        TrainerCreateDto trainerDto = TestData.createTrainerCreateDto();
        trainerDto.getUser().setFirstName(trainer.getUser().getFirstName());

        when(trainerMapper.toEntity(trainerDto)).thenReturn(trainer);
        when(usernameGeneratorService.generateUsername(trainerDto.getUser().getFirstName(), trainerDto.getUser().getLastName())).thenReturn("username");
        when(passwordGenerator.generatePassword()).thenReturn("password");
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.toLoginDto(trainer)).thenReturn(TestData.createUserLoginDto());
        when(passwordEncoder.encode("password")).thenReturn("password");

        trainerService.create(trainerDto);

        ArgumentCaptor<Trainer> trainerCaptor = ArgumentCaptor.forClass(Trainer.class);
        verify(trainerRepository, times(1)).save(trainerCaptor.capture());

        Trainer savedTrainer = trainerCaptor.getValue();
        assertThat(savedTrainer.getUser().getPassword()).isEqualTo("password");
        assertThat(savedTrainer.getUser().getUsername()).isEqualTo("username");
    }

    @Test
    void shouldThrowExceptionWhenCreatingNullTrainer() {
        assertThrows(NullPointerException.class, () -> trainerService.create(null));
    }

    @Test
    void shouldRetrieveTrainerByUsernameSuccessfully() {
        when(trainerMapper.toDto(any())).thenReturn(trainerDto);
        when(trainerRepository.findByUserUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        TrainerDto retrieved = trainerService.getByUsername(trainerDto.getUser().getUsername());

        assertNotNull(retrieved);
        assertEquals(trainerDto.getUser().getUsername(), retrieved.getUser().getUsername());
        verify(trainerRepository, times(1)).findByUserUsername(trainerDto.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenTrainerNotFoundByUsername() {
        when(trainerRepository.findByUserUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.empty());
        String username = trainerDto.getUser().getUsername();
        assertThrows(EntityNotFoundException.class, () -> trainerService.getByUsername(username));
    }

    @Test
    void shouldUpdateTrainerByUsernameSuccessfully() {
        TrainerUpdateDto updatedTrainerDto = TestData.createTrainerUpdateDto();
        TrainerDto updatedTrainerDtoResult = TestData.createTrainerDto();
        when(trainerRepository.findByUserUsername(updatedTrainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        doNothing().when(trainerMapper).updateEntityFromDto(updatedTrainerDto, trainer);
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        when(trainerMapper.toDto(trainer)).thenReturn(updatedTrainerDtoResult);

        TrainerDto result = trainerService.update(updatedTrainerDto);

        verify(trainerRepository, times(1)).save(trainer);
        assertThat(result.getUser().getFirstName()).isEqualTo(updatedTrainerDto.getUser().getFirstName());
        assertThat(result.getUser().getLastName()).isEqualTo(updatedTrainerDto.getUser().getLastName());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTrainer() {
        TrainerUpdateDto updatedTrainerDto = TestData.createTrainerUpdateDto();
        assertThrows(EntityNotFoundException.class, () -> trainerService.update(updatedTrainerDto));
    }

    @Test
    void shouldChangeTrainerStatus() {
        when(trainerRepository.findByUserUsername(trainerDto.getUser().getUsername())).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(trainer)).thenReturn(trainer);
        boolean newStatus = trainerService.changeStatus(trainerDto.getUser().getUsername());
        verify(trainerRepository, times(2)).save(trainer);
        assertFalse(newStatus);
    }

}
