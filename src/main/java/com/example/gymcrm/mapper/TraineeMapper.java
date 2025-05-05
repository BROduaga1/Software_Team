package com.example.gymcrm.mapper;

import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public
interface TraineeMapper {

    TraineeDto toDto(Trainee entity);

    Trainee toEntity(TraineeCreateDto dto);

    @Mapping(ignore = true, target = "user.username")
    void updateEntityFromDto(TraineeUpdateDto traineeDto, @MappingTarget Trainee trainee);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "password", source = "user.password")
    UserLoginDto toLoginDto(Trainee trainee);
}

