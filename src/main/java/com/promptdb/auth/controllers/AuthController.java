package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.dto.UserLoginResponseDTO;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.dto.UserLoginRequest;
import com.promptdb.auth.services.JWTService;
import com.promptdb.auth.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
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
        UserLoginResponseDTO loginResponseDTO = userServices
                .loginUser(userLoginRequest.getUsername(),
                        userLoginRequest.getPassword());

        ApiResponse apiResponse = new ApiResponse(loginResponseDTO);

        HttpHeaders headers = new HttpHeaders();
        ResponseCookie responseCookie = ResponseCookie
                .from("jwt", loginResponseDTO.getToken())
                .httpOnly(true)
                .maxAge(24*60*60)
                .sameSite("Strict")
                .build();

        headers.add(HttpHeaders.SET_COOKIE, responseCookie.toString());

        return new ResponseEntity<>(apiResponse, headers, HttpStatus.OK);
    }
}
