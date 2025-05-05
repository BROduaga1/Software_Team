package com.example.gymcrm.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Data
@ToString
public class UserShortInfoDto {

    @ToString.Exclude
    private String firstName;

    @ToString.Exclude
    private String lastName;

    private String username;
}
