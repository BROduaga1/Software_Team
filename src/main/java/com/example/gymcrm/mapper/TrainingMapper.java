package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Training;
import com.example.gymcrm.dto.training.TrainingDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {TrainerMapper.class, TraineeMapper.class})
public interface TrainingMapper {

    @Mapping(target = "traineeUsername", source = "trainee.user.username")
    @Mapping(target = "trainerUsername", source = "trainer.user.username")
    TrainingDto toDto(Training entity);

    @Mapping(target = "trainee.user.username", source = "traineeUsername")
    @Mapping(target = "trainer.user.username", source = "trainerUsername")
    Training toEntity(TrainingDto dto);

    List<TrainingDto> toDtoList(List<Training> trainings);
}
