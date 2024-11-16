package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.dto.CreateCompanyRequest;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.services.CompanyService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/company")
public class CompanyController {

    private static final Logger log = LoggerFactory.getLogger(CompanyController.class);
    @Autowired
    private CompanyService companyService;

    @PutMapping("")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody @Valid CreateCompanyRequest companyInfo) throws AuthException {

        log.info("creating company");

        CompanyModel companyModel = companyService.createCompany(companyInfo);

        ApiResponse apiResponse = new ApiResponse(companyModel);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
