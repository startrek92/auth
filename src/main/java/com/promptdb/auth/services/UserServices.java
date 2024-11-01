package com.promptdb.auth.services;

import com.promptdb.auth.models.User;
import com.promptdb.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCrypt bCrypt;

    public User createNewUser(String name, Integer age) {
        log.info("creating new user");
        User user = new User(name, age);
        user = userRepository.save(user);
        return user;
    }

    public String loginUser(String username, String password) {
        log.info("login user request: {}, {}", username, password);
        String storedHash = "$2a$10$ycdkuoYcZtFLZqPrq2s3VuiBCWJwEoT8XXxWCmObJkMDuL3Zf/asS";
        if(username.equals("hello") && bCrypt.checkpw(password.getBytes(), storedHash)) {
            return "login success";
        }

        return "login failed";
    }
}
