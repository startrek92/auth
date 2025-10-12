package com.promptdb.auth.services;

import com.promptdb.auth.dto.CreateCompanyRequest;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.repository.repoInterfaces.CompanyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    CompanyRepository repo;

    @InjectMocks
    CompanyService service;

    @Test
    void createCompany_savesAndReturnsEntity() throws Exception {
        CreateCompanyRequest req = new CreateCompanyRequest();
        req.setName("ACME");
        req.setIsActive(true);

        when(repo.save(any(CompanyModel.class))).thenAnswer(invocation -> invocation.getArgument(0, CompanyModel.class));

        CompanyModel saved = service.createCompany(req);
        assertNotNull(saved);
        assertEquals("ACME", saved.getName());
        assertTrue(saved.getIsActive());
        assertNotNull(saved.getCreatedOn());
        assertNotNull(saved.getModifiedOn());

        verify(repo, times(1)).save(any(CompanyModel.class));
    }
}
