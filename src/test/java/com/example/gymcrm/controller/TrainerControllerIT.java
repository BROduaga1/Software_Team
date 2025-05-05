package com.example.gymcrm.controller;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.dto.trainer.TrainerCreateDto;
import com.example.gymcrm.dto.trainer.TrainerUpdateDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainerControllerIT extends BaseH2Test {
    private static final String BASE_URL = "/api/trainers";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserFacade userFacade;

    private MockMvc mockMvc;

    private TrainerCreateDto trainerCreateDto;
    private TrainerUpdateDto trainerUpdateDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        trainerCreateDto = TestData.createTrainerCreateDto();
        trainerUpdateDto = TestData.createTrainerUpdateDto();
    }

    @Test
    void shouldCreateTrainer() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(trainerCreateDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", containsString(trainerCreateDto.getUser().getFirstName())));
    }

    @Test
    void shouldGetTrainerByUsername() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(trainerCreateDto);
        mockMvc.perform(get(BASE_URL + "/{username}", createdTrainerLogin.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.username", is(createdTrainerLogin.getUsername())));
    }

    @Test
    void shouldUpdateTrainer() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(trainerCreateDto);
        trainerUpdateDto.getUser().setUsername(createdTrainerLogin.getUsername());
        mockMvc.perform(put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(trainerUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.firstName", is(trainerUpdateDto.getUser().getFirstName())));
    }

    @Test
    void shouldChangeTrainerStatus() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(trainerCreateDto);
        mockMvc.perform(patch(BASE_URL + "/{username}/status", createdTrainerLogin.getUsername())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetUnassignedTrainers() throws Exception {
        UserLoginDto createdTrainerLogin = userFacade.createTrainer(trainerCreateDto);

        mockMvc.perform(get(BASE_URL + "/{username}/unassigned", createdTrainerLogin.getUsername())
                        .param("traineeUsername", "trainee1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].user.username", hasItem(createdTrainerLogin.getUsername())));
    }
}
