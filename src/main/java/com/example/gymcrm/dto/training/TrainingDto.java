package com.example.gymcrm.dto.training;

import com.example.gymcrm.domain.TrainingType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class TrainingDto {
    private Long id;

    @Schema(description = "The username of the trainer", example = "john.doe")
    @NotNull(message = "Trainer username must not be null")
    private String trainerUsername;

    @Schema(description = "The username of the trainee", example = "jane_doe")
    @NotNull(message = "Trainee username must not be null")
    private String traineeUsername;

    @NotNull(message = "Name must not be null")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    @Schema(description = "The name of the training", example = "Leg day")
    private String name;

    @NotNull(message = "Training type must not be null")
    @Schema(description = "The type of the training", example = "{name:YOGA, id:1}")
    private TrainingType type;

    @NotNull(message = "Date must not be null")
    @Schema(description = "The date of the training", example = "2021-12-31T23:59:59")
    private LocalDateTime date;

    @NotNull(message = "Duration must not be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Schema(description = "The duration of the training in minutes", example = "60")
    private Long durationMinutes;
}
