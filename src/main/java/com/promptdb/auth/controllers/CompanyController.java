package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.dto.CreateCompanyRequest;
import com.promptdb.auth.dto.User.UserInfoResponseDTO;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.services.CompanyService;
import com.promptdb.auth.services.UserServices;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/company")
@Slf4j
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserServices userServices;

    @PutMapping("")
    public ResponseEntity<ApiResponse> createCompany(@RequestBody @Valid CreateCompanyRequest companyInfo) throws AuthException {

        log.info("creating company");

        CompanyModel companyModel = companyService.createCompany(companyInfo);

        ApiResponse apiResponse = new ApiResponse(companyModel);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> listAllCompanyUsers(
            @RequestParam(value = "query", required = false) String searchQuery) throws AuthException {

        log.info("getting all company users :{}", searchQuery);
        List<UserInfoResponseDTO> userInfoResponseDTOList = userServices.getAllUsers(searchQuery);
        ApiResponse apiResponse = new ApiResponse(userInfoResponseDTOList);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
