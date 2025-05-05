package com.example.gymcrm.controller;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.dto.training.TrainingDto;
import com.example.gymcrm.dto.training.TrainingSearchDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.TrainingFacade;
import com.example.gymcrm.facade.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrainingControllerIT extends BaseH2Test {
    private static final String BASE_URL = "/api/trainings";

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private TrainingFacade trainingFacade;

    private MockMvc mockMvc;

    private TrainingDto trainingDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        UserLoginDto trainerLoginDto = userFacade.createTrainer(TestData.createTrainerCreateDto());
        UserLoginDto traineeLoginDto = userFacade.createTrainee(TestData.createTraineeCreateDto());
        trainingDto = TestData.createTrainingDto();
        trainingDto.setTraineeUsername(traineeLoginDto.getUsername());
        trainingDto.setTrainerUsername(trainerLoginDto.getUsername());
    }

    @Test
    void shouldCreateTraining() throws Exception {
        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(trainingDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.traineeUsername", is(trainingDto.getTraineeUsername())))
                .andExpect(jsonPath("$.trainerUsername", is(trainingDto.getTrainerUsername())));
    }

    @Test
    void shouldGetTrainingById() throws Exception {
        TrainingDto createdTraining = trainingFacade.createTraining(trainingDto);
        mockMvc.perform(get(BASE_URL + "/{id}", createdTraining.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdTraining.getId().intValue())));
    }

    @Test
    void shouldSearchTrainings() throws Exception {
        trainingFacade.createTraining(trainingDto);
        TrainingSearchDto searchDto = new TrainingSearchDto();
        searchDto.setIsTrainee(true);
        searchDto.setUsername(trainingDto.getTraineeUsername());

        mockMvc.perform(post(BASE_URL + "/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(searchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].traineeUsername", is(trainingDto.getTraineeUsername())));
    }

    @Test
    void shouldGetAllTrainingTypes() throws Exception {
        mockMvc.perform(get(BASE_URL + "/types")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").exists());
    }
}
