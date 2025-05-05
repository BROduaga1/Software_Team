package com.example.gymcrm.dto.trainer;

import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.user.UserShortInfoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class TrainerShortInfoDto {
    private TrainingType specialization;
    private UserShortInfoDto user;
}
