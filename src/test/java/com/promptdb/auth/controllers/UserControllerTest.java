package com.promptdb.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.dto.User.CurrentUserInfoResponseDTO;
import com.promptdb.auth.dto.User.UserInfoResponseDTO;
import com.promptdb.auth.dto.User.UserUpdateRequestDTO;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.repoInterfaces.UserRepository;
import com.promptdb.auth.services.JWTService;
import com.promptdb.auth.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
class UserControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserRepository userRepository;
    @MockBean private JWTService jwtService;
    @MockBean private UserServices userServices;

    private UserModel mockUser;

    @BeforeEach
    void setup() {
        log.info("Setting up mock UserModel for tests");

        CompanyModel company = new CompanyModel();
        company.setId(10);
        company.setName("PromptDB");

        mockUser = new UserModel();
        mockUser.setId(1);
        mockUser.setName("John");
        mockUser.setAge(30);
        mockUser.setUsername("john123");
        mockUser.setEmail("john@promptdb.com");
        mockUser.setPassword("pass");
        mockUser.setCompanyModel(company);
        mockUser.setIsActive(true);
        mockUser.setIsLocked(false);
        mockUser.setCreatedOn(new Date());
        mockUser.setModifiedOn(new Date());

        log.debug("Mock user prepared: {}", mockUser);
    }

    private String authHeader() {
        return "Bearer test-token";
    }

    @Test
    void createNewUser_success() throws Exception {
        log.info("Executing test: createNewUser_success");

        Mockito.when(userServices.createNewUser("John", 30)).thenReturn(mockUser);

        UserModel req = new UserModel("John", 30);
        log.info("Request body: {}", req);

        mockMvc.perform(put("/user")
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.age").value(30));

        log.info("createNewUser_success passed");
    }

    @Test
    void getCurrentUserInfo_success() throws Exception {
        log.info("Executing test: getCurrentUserInfo_success");

        CurrentUserInfoResponseDTO dto = new CurrentUserInfoResponseDTO(mockUser);
        Mockito.when(userServices.currentUser()).thenReturn(dto);

        mockMvc.perform(get("/user")
                        .header("Authorization", authHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("John"))
                .andExpect(jsonPath("$.data.username").value("john123"))
                .andExpect(jsonPath("$.data.company_id").value(10))
                .andExpect(jsonPath("$.data.company_name").value("PromptDB"))
                .andExpect(jsonPath("$.data.is_active").value(true));

        log.info("getCurrentUserInfo_success passed");
    }

    @Test
    void getUserById_success() throws Exception {
        log.info("Executing test: getUserById_success");

        UserInfoResponseDTO dto = new UserInfoResponseDTO(mockUser);
        Mockito.when(userServices.getUserInfoById(1)).thenReturn(List.of(dto));

        mockMvc.perform(get("/user/1")
                        .header("Authorization", authHeader()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("john123"))
                .andExpect(jsonPath("$.data[0].company_id").value(10));

        log.info("getUserById_success passed");
    }

    @Test
    void updateCurrentUser_success() throws Exception {
        log.info("Executing test: updateCurrentUser_success");

        UserUpdateRequestDTO req =
                new UserUpdateRequestDTO("updatedUser", "Updated Name", "updated@mail.com");

        log.debug("Update request body: {}", req);

        mockUser.setUsername("updatedUser");
        mockUser.setName("Updated Name");
        mockUser.setEmail("updated@mail.com");

        UserInfoResponseDTO dto = new UserInfoResponseDTO(mockUser);
        Mockito.when(userServices.updateCurrentUser(any())).thenReturn(dto);

        mockMvc.perform(post("/user")
                        .header("Authorization", authHeader())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("updatedUser"))
                .andExpect(jsonPath("$.data.name").value("Updated Name"))
                .andExpect(jsonPath("$.data.email").value("updated@mail.com"));

        log.info("updateCurrentUser_success passed");
    }
}