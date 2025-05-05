package com.example.gymcrm.controller;

import com.example.gymcrm.BaseH2Test;
import com.example.gymcrm.TestData;
import com.example.gymcrm.dto.user.ChangeLoginDto;
import com.example.gymcrm.dto.user.UserLoginDto;
import com.example.gymcrm.facade.UserFacade;
import com.example.gymcrm.repository.TrainingTypeRepository;
import com.example.gymcrm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerIT extends BaseH2Test {
    private static final String BASE_URL = "/api/auth/";
    @Autowired
    UserRepository userRepository;
    @Autowired
    TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private UserFacade userFacade;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void shouldAuthenticateUser() throws Exception {
        UserLoginDto loginDto = TestData.getExistingAdminLoginDto();
        mockMvc.perform(post(BASE_URL + "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.refreshToken").isString());
    }

    @Test
    void shouldChangePassword() throws Exception {
        UserLoginDto loginDto = userFacade.createTrainee(TestData.createTraineeCreateDto());
        ChangeLoginDto changeLoginDto = new ChangeLoginDto(loginDto.getUsername(), loginDto.getPassword(), "newPassword");
        mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + "change-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(TestData.asJsonString(changeLoginDto)))
                .andExpect(status().isOk());
    }
}
