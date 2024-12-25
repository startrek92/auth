package com.promptdb.auth.services;

import com.promptdb.auth.dto.CreateCompanyRequest;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.repository.repoInterfaces.CompanyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Transactional(rollbackOn = AuthException.class)
    public CompanyModel createCompany(CreateCompanyRequest createCompanyRequest) throws AuthException {
        CompanyModel companyModel = createCompanyRequest.createCompanyModel();
        companyModel = companyRepository.save(companyModel);
        return companyModel;
    }
}
