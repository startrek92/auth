package com.promptdb.auth.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.promptdb.auth.dto.CreateCompanyRequest;
import com.promptdb.auth.dto.User.UserInfoResponseDTO;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.services.CompanyService;
import com.promptdb.auth.services.UserServices;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyController.class)
@AutoConfigureMockMvc(addFilters = false)
@Slf4j
public class CompanyControllerTest {

    @MockBean
    private CompanyService companyService;

    @MockBean
    private UserServices userServices;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CompanyModel createCompany(Integer id, String name, boolean active) {
        CompanyModel company = new CompanyModel();
        company.setId(id);
        company.setName(name);
        company.setIsActive(active);
        return company;
    }

    private UserInfoResponseDTO createUser(Integer id, String name, String username, CompanyModel company) {
        UserModel user = new UserModel();
        user.setId(id);
        user.setName(name);
        user.setUsername(username);
        user.setCompanyModel(company);
        return new UserInfoResponseDTO(user);
    }

    @Test
    void createNewCompany_success() throws Exception {
        CreateCompanyRequest newRequestCompany = new CreateCompanyRequest("testCompany", true);
        CompanyModel newCompany = new CompanyModel("testCompany", true);

        Mockito.when(companyService.createCompany(any(CreateCompanyRequest.class)))
                .thenReturn(newCompany);

        mockMvc.perform(
                        put("/company")
                                .header("Authorization", "test-token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newRequestCompany))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data.name").value("testCompany"));
    }

    @Test
    void listAllCompanyUsers_success() throws Exception {
        log.info("testing listAllCompanyUsers");

        CompanyModel testCompany = createCompany(123, "testCompany", true);

        List<UserInfoResponseDTO> users = List.of(
                createUser(1, "John", "john123", testCompany),
                createUser(2, "Doe", "doe456", testCompany)
        );

        Mockito.when(userServices.getAllUsers("prompt"))
                .thenReturn(users);

        mockMvc.perform(
                        get("/company/user")
                                .param("query", "prompt")
                                .header("Authorization", "test-token")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("john123"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].username").value("doe456"));
    }
}