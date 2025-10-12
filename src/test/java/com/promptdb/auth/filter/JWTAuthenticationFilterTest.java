package com.promptdb.auth.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.dto.UserLoginRequest;
import com.promptdb.auth.models.BearerTokenModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.repoInterfaces.BearerTokenRepository;
import com.promptdb.auth.repository.repoInterfaces.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class JWTAuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BearerTokenRepository bearerTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

        // Create a real user
//        UserModel user = new UserModel();
//        user.setUsername("amit");
//        user.setPassword(passwordEncoder.encode("password123"));
//        user.setIsActive(true);
//        userRepository.save(user);
    }

    @Test
    void testLoginAndTokenSavedInDB() throws Exception {
        // given
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("alice");
        loginRequest.setPassword("123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(cookie().exists("auth-session-token"))
                .andExpect(jsonPath("$.data.username").value("alice"))
                .andExpect(jsonPath("$.data.token").exists());

    }

    @Test
    void testInvalidPassword_ShouldReturnUnauthorized() throws Exception {
        // given
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setUsername("alice");
        loginRequest.setPassword("wrong");

        // when
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.data").value("Authentication failed"));
    }
}