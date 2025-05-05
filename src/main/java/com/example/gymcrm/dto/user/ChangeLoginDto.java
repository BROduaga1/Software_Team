package com.example.gymcrm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChangeLoginDto {

    @NotNull(message = "username is required")
    @Schema(description = "The username of the user", example = "john.doe")
    private String username;

    @NotNull(message = "password is required")
    @ToString.Exclude
    @Schema(description = "The old password of the user", example = "password")
    private String oldPassword;

    @NotNull(message = "password is required")
    @ToString.Exclude
    @Schema(description = "The new password of the user", example = "password2")
    private String newPassword;
}
