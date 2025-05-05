package com.example.gymcrm.dto.training;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
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
public class TrainingSearchDto {
    @Nullable
    @Schema(description = "The username of the user", example = "john.doe")
    private String username;
    @Nullable
    @Schema(description = "The first name of the user", example = "john")
    private LocalDateTime fromDate;
    @Nullable
    @Schema(description = "The last name of the user", example = "doe")
    private LocalDateTime toDate;
    @Nullable
    @Schema(description = "The active state of the user", example = "true")
    private Boolean isTrainee;

}
