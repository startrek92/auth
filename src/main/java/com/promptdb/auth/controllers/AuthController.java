package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.dto.UserLoginRequest;
import com.promptdb.auth.services.JWTService;
import com.promptdb.auth.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserServices userServices;

    @Autowired
    JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody UserLoginRequest userLoginRequest) throws AuthException {
        String loginStatus = userServices
                .loginUser(userLoginRequest.getUsername(),
                        userLoginRequest.getPassword());

        ApiResponse apiResponse = new ApiResponse(loginStatus);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
