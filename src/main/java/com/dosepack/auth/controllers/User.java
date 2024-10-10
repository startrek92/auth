package com.dosepack.auth.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class User {

    @GetMapping("/current")
    public String currentUser(Principal principal) {
        return principal.getName();
    }
}
