package com.promptdb.auth.controllers.auth;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.schemas.UserLoginRequest;
import com.promptdb.auth.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class Auth {

    @Autowired
    UserServices userServices;

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest userLoginRequest) throws AuthException {
        String loginStatus = userServices
                .loginUser(userLoginRequest.getUsername(),
                        userLoginRequest.getPassword());

        return loginStatus;
    }
}
