package com.example.gymcrm.service;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
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
import com.example.gymcrm.service.impl.TraineeServiceImpl;
import com.example.gymcrm.util.PasswordGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static com.example.gymcrm.TestData.TRAINEE_NOT_FOUND;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    TrainerRepository trainerRepository;
    @Mock
    TrainerMapper trainerMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    private TraineeServiceImpl traineeService;
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private PasswordGenerator passwordGenerator;
    @Mock
    private TraineeMapper traineeMapper;
    @Mock
    private UsernameGeneratorService usernameGeneratorService;
    private TraineeUpdateDto traineeUpdateDto;
    private Trainee trainee;

    @BeforeEach
    void setUp() {
        traineeUpdateDto = TestData.createTraineeUpdateDto();
        trainee = TestData.createTrainee();
    }

    @Test
    void shouldSaveTraineeAndGeneratePasswordAndUsername() {
        UserLoginDto loginDto = TestData.createUserLoginDto();
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        traineeCreateDto.getUser().setFirstName(trainee.getUser().getFirstName());

        when(traineeMapper.toEntity(traineeCreateDto)).thenReturn(trainee);
        when(usernameGeneratorService.generateUsername(traineeCreateDto.getUser().getFirstName(), traineeCreateDto.getUser().getLastName())).thenReturn("username");
        when(passwordGenerator.generatePassword()).thenReturn("password");
        when(traineeMapper.toLoginDto(trainee)).thenReturn(loginDto);
        when(passwordEncoder.encode("password")).thenReturn("password");

        UserLoginDto userLoginDto = traineeService.create(traineeCreateDto);

        ArgumentCaptor<Trainee> traineeCaptor = ArgumentCaptor.forClass(Trainee.class);
        verify(traineeRepository, times(1)).save(traineeCaptor.capture());

        Trainee savedTrainee = traineeCaptor.getValue();
        assertThat(savedTrainee.getUser().getPassword()).isEqualTo("password");
        assertThat(savedTrainee.getUser().getUsername()).isEqualTo("username");
        assertThat(userLoginDto.getUsername()).isEqualTo(loginDto.getUsername());
        assertThat(userLoginDto.getPassword()).isEqualTo(loginDto.getPassword());
    }

    @Test
    void shouldThrowExceptionWhenSavingNullTrainee() {
        assertThrows(NullPointerException.class, () -> traineeService.create(null));
    }

    @Test
    void shouldRetrieveTraineeByUsername() {
        when(traineeMapper.toDto(any())).thenReturn(TestData.createTraineeDto());
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        TraineeDto retrieved = traineeService.getByUsername(trainee.getUser().getUsername());

        assertNotNull(retrieved);
        assertEquals(trainee.getUser().getUsername(), retrieved.getUser().getUsername());
        verify(traineeRepository, times(1)).findByUserUsername(trainee.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenTraineeNotFoundByUsername() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.empty());
        String username = trainee.getUser().getUsername();
        assertThrows(EntityNotFoundException.class, () -> traineeService.getByUsername(username));
    }

    @Test
    void shouldUpdateTraineeByUsername() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(traineeMapper.toDto(trainee)).thenReturn(TestData.createTraineeDto());
        when(traineeRepository.save(trainee)).thenReturn(trainee);

        TraineeDto result = traineeService.update(traineeUpdateDto);

        verify(traineeRepository, times(1)).save(any());
        assertThat(result).isEqualTo(TestData.createTraineeDto());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistentTrainee() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> traineeService.update(traineeUpdateDto));
    }

    @Test
    void shouldDeleteTraineeByUsername() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        traineeService.deleteByUsername(trainee.getUser().getUsername());
        verify(traineeRepository, times(1)).deleteByUserUsername(trainee.getUser().getUsername());
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentTrainee() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.empty());
        String username = trainee.getUser().getUsername();
        assertThrows(EntityNotFoundException.class, () -> traineeService.deleteByUsername(username), TRAINEE_NOT_FOUND + username);
    }

    @Test
    void shouldChangeTraineeStatus() {
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(trainee)).thenReturn(trainee);
        boolean newStatus = traineeService.changeStatus(trainee.getUser().getUsername());
        verify(traineeRepository, times(2)).save(trainee);
        assertFalse(newStatus);
    }

    @Test
    void shouldUpdateTrainersListByUsername() {
        List<String> trainerUsernames = List.of("trainer1", "trainer2");
        when(traineeRepository.findByUserUsername(trainee.getUser().getUsername())).thenReturn(Optional.of(trainee));
        when(trainerRepository.findByUserUsername(trainerUsernames.get(0))).thenReturn(Optional.of(TestData.createTrainer()));
        when(trainerRepository.findByUserUsername(trainerUsernames.get(1))).thenReturn(Optional.of(TestData.createTrainer()));

        List<TrainerShortInfoDto> updatedTrainers = traineeService.updateTrainersListByUsername(trainee.getUser().getUsername(), trainerUsernames);
        verify(traineeRepository, times(1)).save(trainee);
        assertNotNull(updatedTrainers);
    }

}
