package com.dosepack.auth.controllers;

import com.dosepack.auth.models.UserModel;
import com.dosepack.auth.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/auth")
public class Auth {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public List<UserModel> loginUser() {
        return this.authService.getUsers();
    }
}
