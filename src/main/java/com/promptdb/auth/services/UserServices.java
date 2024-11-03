package com.promptdb.auth.services;

import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.exceptions.ErrorCodes;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.UserRepository;
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
    private BCrypt bCrypt;

    public UserModel createNewUser(String name, Integer age) {
        log.info("creating new user");
        UserModel user = new UserModel(name, age);
        user = userRepository.save(user);
        return user;
    }

    public String loginUser(String username, String password) throws AuthException {
        log.info("login user request: {}, {}", username, password);
        String storedHash = "$2a$10$ycdkuoYcZtFLZqPrq2s3VuiBCWJwEoT8XXxWCmObJkMDuL3Zf/asS";
        try {
            if (username.equals("hello") && bCrypt.checkpw(password.getBytes(), storedHash)) {
                return "login success";
            } else {
                throw new AuthException(
                        HttpStatus.UNAUTHORIZED,
                        ErrorCodes.AUTH_INVALID_CREDENTIALS.getErrorCode(),
                        ErrorCodes.AUTH_INVALID_CREDENTIALS.getErrorDescription());
            }
        } catch (AuthException exc) {
            throw exc;
        }
    }
}
