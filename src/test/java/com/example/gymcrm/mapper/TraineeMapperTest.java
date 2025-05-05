package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.domain.User;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TraineeMapperTest {

    private final TraineeMapperImpl mapper = new TraineeMapperImpl();

    @Test
    void shouldMapTraineeToTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        TraineeDto traineeDto = mapper.toDto(trainee);
        Assertions.assertThat(trainee.getId()).isEqualTo(traineeDto.getId());
        Assertions.assertThat(trainee.getUser().getUsername()).isEqualTo(traineeDto.getUser().getUsername());
    }

    @Test
    void shouldMapTraineeCreateDtoToTrainee() {
        TraineeCreateDto traineeCreateDto = TestData.createTraineeCreateDto();
        Trainee trainee = mapper.toEntity(traineeCreateDto);
        Assertions.assertThat(traineeCreateDto.getUser().getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        Assertions.assertThat(traineeCreateDto.getUser().getLastName()).isEqualTo(trainee.getUser().getLastName());
    }

    @Test
    void shouldUpdateTraineeFromTraineeUpdateDto() {
        TraineeUpdateDto traineeUpdateDto = TestData.createTraineeUpdateDto();
        Trainee trainee = TestData.createTrainee();
        mapper.updateEntityFromDto(traineeUpdateDto, trainee);
        Assertions.assertThat(traineeUpdateDto.getUser().getFirstName()).isEqualTo(trainee.getUser().getFirstName());
    }

    @Test
    void shouldHandleNullTraineeToTraineeDto() {
        TraineeDto traineeDto = mapper.toDto(null);
        Assertions.assertThat(traineeDto).isNull();
    }

    @Test
    void shouldHandleNullTraineeCreateDtoToTrainee() {
        Trainee trainee = mapper.toEntity(null);
        Assertions.assertThat(trainee).isNull();
    }

    @Test
    void shouldHandleNullTraineeUpdateDtoToTrainee() {
        Trainee trainee = TestData.createTrainee();
        mapper.updateEntityFromDto(null, trainee);
        Assertions.assertThat(trainee).isNotNull();
        Assertions.assertThat(trainee.getUser().getUsername()).isNotNull();
    }

    @Test
    void shouldMapTraineeToUserLoginDto() {
        Trainee trainee = TestData.createTrainee();
        UserLoginDto userLoginDto = mapper.toLoginDto(trainee);
        Assertions.assertThat(userLoginDto.getUsername()).isEqualTo(trainee.getUser().getUsername());
        Assertions.assertThat(userLoginDto.getPassword()).isEqualTo(trainee.getUser().getPassword());
    }

    @Test
    void shouldHandleNullTraineeToUserLoginDto() {
        UserLoginDto userLoginDto = mapper.toLoginDto(null);
        Assertions.assertThat(userLoginDto).isNull();
    }

    @Test
    void shouldMapTraineeTrainersToTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        List<Trainer> trainers = List.of(TestData.createTrainer());
        trainee.setTrainers(trainers);
        TraineeDto traineeDto = mapper.toDto(trainee);
        for (int i = 0; i < trainee.getTrainers().size(); i++) {
            Assertions.assertThat(traineeDto.getTrainers().get(i).getUser().getFirstName())
                    .isEqualTo(trainee.getTrainers().get(i).getUser().getFirstName());
            Assertions.assertThat(traineeDto.getTrainers().get(i).getUser().getLastName())
                    .isEqualTo(trainee.getTrainers().get(i).getUser().getLastName());
        }
    }

    @Test
    void shouldHandleNullTraineeTrainersToTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        trainee.setTrainers(null);
        TraineeDto traineeDto = mapper.toDto(trainee);
        Assertions.assertThat(traineeDto.getTrainers()).isNull();
    }

    @Test
    void shouldReturnNullWhenUserCreateDtoIsNull() {
        User user = mapper.userCreateDtoToUser(null);
        Assertions.assertThat(user).isNull();
    }

    @Test
    void shouldNotUpdateUserWhenUserUpdateDtoIsNull() {
        User user = TestData.createTrainee().getUser();
        mapper.userUpdateDtoToUser(null, user);
        Assertions.assertThat(user).isNotNull();
        Assertions.assertThat(user.getFirstName()).isNotNull();
        Assertions.assertThat(user.getLastName()).isNotNull();
    }

    @Test
    void shouldReturnNullUsernameWhenTraineeUserIsNullForLoginDto() {
        Trainee trainee = TestData.createTrainee();
        trainee.setUser(null);
        UserLoginDto userLoginDto = mapper.toLoginDto(trainee);
        assertThat(userLoginDto.getUsername()).isNull();
    }

    @Test
    void shouldReturnNullUsernameWhenTraineeUserUsernameIsNullForLoginDto() {
        Trainee trainee = TestData.createTrainee();
        trainee.getUser().setUsername(null);
        UserLoginDto userLoginDto = mapper.toLoginDto(trainee);
        assertThat(userLoginDto.getUsername()).isNull();
    }

    @Test
    void shouldReturnNullUserWhenTraineeUserIsNullForTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        trainee.setUser(null);
        TraineeDto traineeDto = mapper.toDto(trainee);
        assertThat(traineeDto.getUser()).isNull();
    }

    @Test
    void shouldReturnNullUsernameWhenTraineeUserUsernameIsNullForTraineeDto() {
        Trainee trainee = TestData.createTrainee();
        trainee.getUser().setUsername(null);
        TraineeDto traineeDto = mapper.toDto(trainee);
        assertThat(traineeDto.getUser().getUsername()).isNull();
    }
}
