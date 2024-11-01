package com.promptdb.auth.services;

import com.promptdb.auth.models.User;
import com.promptdb.auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    public User createNewUser(String name, Integer age) {
        log.info("creating new user");
        User user = new User(name, age);
        user = userRepository.save(user);
        return user;
    }
}
