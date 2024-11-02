package com.promptdb.auth.controller.user;

import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.UserRepository;
import com.promptdb.auth.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServices userServices;

    @PutMapping("")
    public UserModel createNewUser(@RequestBody UserModel user) {
        log.info("in user controller, creating new user: {}", user.toString());
        UserModel newUser = userServices.createNewUser(user.getName(), user.getAge());
        return newUser;
    }
}
