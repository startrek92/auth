package com.promptdb.auth.services;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.UserRepository;
import com.promptdb.auth.utils.BCryptPasswordEncryptorImpl;
import jakarta.transaction.Transactional;
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

    @Transactional
    public UserModel createNewUser(String name, Integer age) throws AuthException {
        log.info("creating new user");
        UserModel user = new UserModel(name, age);
        user = userRepository.save(user);
        if(name.equals("error")) {
            AuthException exception = new AuthException(HttpStatus.INTERNAL_SERVER_ERROR,
                    ErrorCodes.INTERNAL_SERVER_ERROR);
            log.info("raising exception: {0}", exception);
            throw exception;
        }
        return user;
    }

    public String loginUser(String username, String password) throws AuthException {
        log.info("login user request: {}, {}", username, password);
        AuthException authException = new AuthException(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.AUTH_INVALID_CREDENTIALS);

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
