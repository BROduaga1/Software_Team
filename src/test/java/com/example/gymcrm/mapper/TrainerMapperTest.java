package com.example.gymcrm.mapper;

import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TrainerMapperTest {
    private final TrainerMapper mapper = new TrainerMapperImpl();

    @Test
    void shouldMapTrainerToTrainerDto() {
        Trainer trainer = TestData.createTrainer();
        TrainerDto trainerDto = mapper.toDto(trainer);
        assertThat(trainer.getId()).isEqualTo(trainerDto.getId());
        assertThat(trainer.getUser().getUsername()).isEqualTo(trainerDto.getUser().getUsername());
    }

    @Test
    void shouldMapTrainerCreateDtoToTrainer() {
        TrainerCreateDto trainerCreateDto = TestData.createTrainerCreateDto();
        Trainer trainer = mapper.toEntity(trainerCreateDto);
        assertThat(trainerCreateDto.getUser().getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(trainerCreateDto.getUser().getLastName()).isEqualTo(trainer.getUser().getLastName());
    }

    @Test
    void shouldUpdateTrainerFromTrainerUpdateDto() {
        TrainerUpdateDto trainerUpdateDto = TestData.createTrainerUpdateDto();
        Trainer trainer = TestData.createTrainer();
        mapper.updateEntityFromDto(trainerUpdateDto, trainer);
        assertThat(trainerUpdateDto.getUser().getFirstName()).isEqualTo(trainer.getUser().getFirstName());
    }

    @Test
    void shouldHandleNullTrainerToTrainerDto() {
        TrainerDto trainerDto = mapper.toDto(null);
        assertThat(trainerDto).isNull();
    }

    @Test
    void shouldHandleNullTrainerCreateDtoToTrainer() {
        Trainer trainer = mapper.toEntity(null);
        assertThat(trainer).isNull();
    }

    @Test
    void shouldHandleNullTrainerUpdateDtoToTrainer() {
        Trainer trainer = TestData.createTrainer();
        mapper.updateEntityFromDto(null, trainer);
        assertThat(trainer).isNotNull();
        assertThat(trainer.getUser().getUsername()).isNotNull();
    }

    @Test
    void shouldMapTrainersListToTrainerDtoList() {
        Trainer trainer = TestData.createTrainer();
        assertThat(mapper.toDtoList(List.of(trainer)).get(0).getUser().getFirstName())
                .isEqualTo(trainer.getUser().getFirstName());
    }

    @Test
    void shouldMapEmptyTrainersListToEmptyTrainerDtoList() {
        List<TrainerShortInfoDto> trainerDtos = mapper.toDtoList(List.of());
        Assertions.assertThat(trainerDtos).isEmpty();
    }

    @Test
    void shouldHandleNullUserDtoToUser() {
        Trainer trainer = TestData.createTrainer();
        trainer.setUser(null);
        TrainerDto trainerDto = mapper.toDto(trainer);
        Assertions.assertThat(trainerDto).isNotNull();
        Assertions.assertThat(trainerDto.getUser()).isNull();
    }

    @Test
    void shouldHandleNullUserToUserDto() {
        TrainerCreateDto trainerDto = TestData.createTrainerCreateDto();
        trainerDto.setUser(null);
        Trainer trainer = mapper.toEntity(trainerDto);
        Assertions.assertThat(trainer).isNotNull();
        Assertions.assertThat(trainer.getUser()).isNull();
    }

    @Test
    void shouldMapTrainerToUserLoginDto() {
        Trainer trainer = TestData.createTrainer();
        UserLoginDto userLoginDto = mapper.toLoginDto(trainer);
        Assertions.assertThat(userLoginDto.getUsername()).isEqualTo(trainer.getUser().getUsername());
        Assertions.assertThat(userLoginDto.getPassword()).isEqualTo(trainer.getUser().getPassword());
    }

    @Test
    void shouldHandleNullTrainerToUserLoginDto() {
        UserLoginDto userLoginDto = mapper.toLoginDto(null);
        Assertions.assertThat(userLoginDto).isNull();
    }

    @Test
    void shouldMapTrainersListToTrainerShortInfoDtoList() {
        Trainer trainer = TestData.createTrainer();
        List<TrainerShortInfoDto> trainerShortInfoDtos = mapper.toDtoList(List.of(trainer));
        Assertions.assertThat(trainerShortInfoDtos).hasSize(1);
        Assertions.assertThat(trainerShortInfoDtos.get(0).getUser().getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        Assertions.assertThat(trainerShortInfoDtos.get(0).getUser().getLastName()).isEqualTo(trainer.getUser().getLastName());
    }

    @Test
    void shouldHandleEmptyTrainersListToTrainerShortInfoDtoList() {
        List<TrainerShortInfoDto> trainerShortInfoDtos = mapper.toDtoList(List.of());
        Assertions.assertThat(trainerShortInfoDtos).isEmpty();
    }

    @Test
    void shouldReturnNullWhenTrainerIsNullForTrainerShortInfoDtoList() {
        Trainer trainer = TestData.createTrainer();
        trainer.setUser(null);
        List<TrainerShortInfoDto> trainerShortInfoDtos = mapper.toDtoList(List.of(trainer));
        Assertions.assertThat(trainerShortInfoDtos.get(0).getUser()).isNull();
    }

    @Test
    void shouldHandleEmptyTrainersListForTrainerShortInfoDtoList() {
        List<TrainerShortInfoDto> trainerShortInfoDtos = mapper.toDtoList(List.of());
        Assertions.assertThat(trainerShortInfoDtos).isEmpty();
    }

    @Test
    void shouldReturnNullUsernameWhenTrainerUserIsNullForLoginDto() {
        Trainer trainer = TestData.createTrainer();
        trainer.setUser(null);
        UserLoginDto userLoginDto = mapper.toLoginDto(trainer);
        Assertions.assertThat(userLoginDto.getUsername()).isNull();
    }

    @Test
    void shouldReturnNullUsernameWhenTrainerUserUsernameIsNullForLoginDto() {
        Trainer trainer = TestData.createTrainer();
        trainer.getUser().setUsername(null);
        UserLoginDto userLoginDto = mapper.toLoginDto(trainer);
        Assertions.assertThat(userLoginDto.getUsername()).isNull();
    }

    @Test
    void shouldReturnNullUserWhenTrainerUserIsNullForTrainerDto() {
        Trainer trainer = TestData.createTrainer();
        trainer.setUser(null);
        TrainerDto trainerDto = mapper.toDto(trainer);
        Assertions.assertThat(trainerDto.getUser()).isNull();
    }

    @Test
    void shouldReturnNullUsernameWhenTrainerUserUsernameIsNullForTrainerDto() {
        Trainer trainer = TestData.createTrainer();
        trainer.getUser().setUsername(null);
        TrainerDto trainerDto = mapper.toDto(trainer);
        Assertions.assertThat(trainerDto.getUser().getUsername()).isNull();
    }

}
