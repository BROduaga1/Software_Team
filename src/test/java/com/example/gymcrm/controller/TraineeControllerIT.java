package com.example.gymcrm.controller;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.domain.Trainee;
import com.example.gymcrm.dto.trainee.TraineeCreateDto;
import com.example.gymcrm.dto.trainee.TraineeDto;
import com.example.gymcrm.dto.trainee.TraineeUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.UserFacade;
import com.example.gymcrm.repository.TraineeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TraineeControllerIT extends BaseH2Test {
    private static final String BASE_URL = "/api/trainees";

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserFacade userFacade;
    private MockMvc mockMvc;
    @Autowired
    private TraineeRepository traineeRepository;
    private TraineeCreateDto traineeCreateDto;
    private TraineeUpdateDto traineeUpdateDto;
    private TraineeDto traineeDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        traineeCreateDto = TestData.createTraineeCreateDto();
        traineeUpdateDto = TestData.createTraineeUpdateDto();
        traineeDto = TestData.createTraineeDto();
    }

    @Test
    void shouldCreateTrainee() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(traineeCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", containsString(traineeCreateDto.getUser().getFirstName())));
    }

    @Test
    void shouldGetTraineeByUsername() throws Exception {
        UserLoginDto trainee = userFacade.createTrainee(TestData.createTraineeCreateDto());
        mockMvc.perform(get(BASE_URL + "/{username}", trainee.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(trainee.getUsername())));
    }

    @Test
    void shouldUpdateTrainee() throws Exception {
        String username = userFacade.createTrainee(TestData.createTraineeCreateDto()).getUsername();
        traineeUpdateDto.getUser().setUsername(username);
        mockMvc.perform(put(BASE_URL + "/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(traineeUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.firstName", is(traineeUpdateDto.getUser().getFirstName())));
    }

    @Test
    void shouldDeleteTrainee() throws Exception {
        String username = userFacade.createTrainee(TestData.createTraineeCreateDto()).getUsername();
        mockMvc.perform(delete(BASE_URL + "/{username}", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        Optional<Trainee> deletedTrainee = traineeRepository.findByUserUsername(traineeDto.getUser().getUsername());
        assertTrue(deletedTrainee.isEmpty());
    }

    @Test
    void shouldChangeTraineeStatus() throws Exception {
        String username = userFacade.createTrainee(TestData.createTraineeCreateDto()).getUsername();
        mockMvc.perform(patch(BASE_URL + "/{username}/status", username)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateTrainersList() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(TestData.createTrainerCreateDto());
        String traineeUsername = userFacade.createTrainee(TestData.createTraineeCreateDto()).getUsername();
        List<String> trainerUsernames = List.of(createdTrainerLogin.getUsername());

        mockMvc.perform(put(BASE_URL + "/{username}/trainers", traineeUsername)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(trainerUsernames)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username", is(createdTrainerLogin.getUsername())));
    }

    @Test
    void shouldGetAvailableTrainers() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(TestData.createTrainerCreateDto());

        mockMvc.perform(get(BASE_URL + "/{username}/trainers", traineeDto.getUser().getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].user.username", hasItem(createdTrainerLogin.getUsername())));
    }


}
