package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Trainer;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerDto;
import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDto toDto(Trainer entity);

    Trainer toEntity(TrainerCreateDto dto);

    @Mapping(ignore = true, target = "user.username")
    void updateEntityFromDto(TrainerUpdateDto trainerDto, @MappingTarget Trainer trainer);

    List<TrainerShortInfoDto> toDtoList(List<Trainer> trainers);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    UserLoginDto toLoginDto(Trainer entity);
}
