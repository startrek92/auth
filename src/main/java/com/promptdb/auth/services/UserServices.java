package com.promptdb.auth.services;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.UserRepository;
import com.promptdb.auth.utils.BCryptPasswordEncryptorImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncryptorImpl bCrypt;

    public UserModel createNewUser(String name, Integer age) {
        log.info("creating new user");
        UserModel user = new UserModel(name, age);
        user = userRepository.save(user);
        return user;
    }

    public String loginUser(String username, String password) throws AuthException {
        log.info("login user request: {}, {}", username, password);
        AuthException authException = new AuthException(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.AUTH_INVALID_CREDENTIALS.getErrorCode(),
                ErrorCodes.AUTH_INVALID_CREDENTIALS.getErrorDescription());

        UserModel userModel = userRepository.findByUsername(username);
        if (userModel == null) {
            log.info("username: {} not found", username);
            throw authException;
        }
        if (bCrypt.checkHash(password, userModel.getPassword())) {
            return "login success";
        } else {
            throw authException;
        }
    }
}
