package com.example.gymcrm.dto.trainer;

import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.trainee.TraineeShortInfoDto;
import com.example.gymcrm.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class TrainerDto {
    private Long id;
    private TrainingType specialization;
    private UserDto user;
    private List<TraineeShortInfoDto> trainees;
}
