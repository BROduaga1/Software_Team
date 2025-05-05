package com.example.gymcrm.dto.trainee;

import com.example.gymcrm.dto.trainer.TrainerShortInfoDto;
import com.example.gymcrm.dto.user.UserDto;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.List;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class TraineeDto {
    private Long id;

    @Nullable
    @ToString.Exclude
    private LocalDate dateOfBirth;

    @ToString.Exclude
    private String address;

    private UserDto user;

    private List<TrainerShortInfoDto> trainers;

}
