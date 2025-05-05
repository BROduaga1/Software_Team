package com.example.gymcrm.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
@ToString
public class UserUpdateDto {
    @NotNull(message = "Username is required")
    @Schema(description = "The username of the user", example = "username")
    private String username;

    @ToString.Exclude
    @Schema(description = "The first name of the user", example = "john")
    @NotNull(message = "First name must not be null")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @ToString.Exclude
    @Schema(description = "The first name of the user", example = "doe")
    @NotNull(message = "Last name must not be null")
    @Size(min = 1, max = 50, message = "Last name must be between 1 and 50 characters")
    private String lastName;

    @NotNull(message = "Active status must not be null")
    @Schema(description = "The active state of the user", example = "true")
    private Boolean isActive;

}
