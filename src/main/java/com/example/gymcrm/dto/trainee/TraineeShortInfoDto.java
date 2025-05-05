package com.example.gymcrm.dto.trainee;

import com.example.gymcrm.dto.user.UserShortInfoDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Data
@ToString
public class TraineeShortInfoDto {
    private UserShortInfoDto user;
}
