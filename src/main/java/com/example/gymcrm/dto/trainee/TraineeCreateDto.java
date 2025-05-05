package com.example.gymcrm.dto.trainee;

import com.example.gymcrm.dto.user.UserCreateDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TraineeCreateDto {
    @NotNull(message = "User is required")
    @Valid
    private UserCreateDto user;

    @ToString.Exclude
    @Schema(description = "The date of birth of the trainee", example = "2000-01-01")
    private LocalDate dateOfBirth;

    @ToString.Exclude
    @Schema(description = "The address of the trainee", example = "Some address")
    private String address;
}
