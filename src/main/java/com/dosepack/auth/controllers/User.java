package com.dosepack.auth.controllers;

import com.dosepack.auth.schema.CurrentUserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class User {

    @GetMapping("/current")
    public CurrentUserInfo currentUser(Principal principal) {
        CurrentUserInfo currentUserInfo = new CurrentUserInfo("12", principal.getName(), principal.getName());
        return currentUserInfo;
    }
}
