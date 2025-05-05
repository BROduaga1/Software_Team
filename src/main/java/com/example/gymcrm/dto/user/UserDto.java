package com.example.gymcrm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class UserDto {
    @ToString.Exclude
    private Long id;
    @ToString.Exclude
    private String firstName;
    @ToString.Exclude
    private String lastName;
    private String username;
    private Boolean isActive;
}
