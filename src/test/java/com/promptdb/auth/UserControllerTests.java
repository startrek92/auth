package com.promptdb.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetUserById() throws Exception {
        UserModel userModel = new UserModel("dummyUser", 34);
        userModel.setIsLocked(false);
        userModel.setIsActive(true);
        CompanyModel companyModel = new CompanyModel();
        companyModel.setId(123);
        companyModel.setName("dummy company");
        userModel.setId(5684);
        userModel.setCompanyModel(companyModel);

        mockMvc.perform(
                post("/api/user/5684")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userModel))
                )
                .andExpect(
                        status()
                                .isBadRequest()
                );

        mockMvc.perform(
                get("/api/user/5684")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer dummyToken")
        ).andExpect(status().isUnauthorized());

    }
}
