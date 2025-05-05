package com.example.gymcrm.dto.trainer;

import com.example.gymcrm.domain.TrainingType;
import com.example.gymcrm.dto.user.UserCreateDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TrainerCreateDto {

    @NotNull(message = "User is required")
    @Valid
    private UserCreateDto user;

    @NotNull(message = "specialization is required")
    private TrainingType specialization;
}
