package com.promptdb.auth.services;

import com.promptdb.auth.dto.UserLoginResponseDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServices {

    private static final Logger log = LoggerFactory.getLogger(UserServices.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncryptorImpl bCrypt;

    @Autowired JWTService jwtService;

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

    public UserLoginResponseDTO loginUser(String username, String password) throws AuthException {
        log.info("login user request: {}, {}", username, password);
        AuthException authException = new AuthException(
                HttpStatus.UNAUTHORIZED,
                ErrorCodes.AUTH_INVALID_CREDENTIALS);

        UserModel userModel = userRepository.findByUsername(username);
        if (userModel == null) {
            log.info("username: {} not found", username);
            throw authException;
        }

        validateUserAccount(userModel);

        if (bCrypt.checkHash(password, userModel.getPassword())) {
            Map<String, Object> extraClaims = userModel.getLoginJwtClaims();
            String token = jwtService.generateToken(username, extraClaims);
            UserLoginResponseDTO userLoginResponseDTO = new UserLoginResponseDTO();
            userLoginResponseDTO.setUsername(userModel.getUsername());
            userLoginResponseDTO.setName(userModel.getName());
            userLoginResponseDTO.setEmail(userModel.getEmail());
            userLoginResponseDTO.setToken(token);
            userLoginResponseDTO.setSessionId(jwtService.generateSessionId(userModel));
            return userLoginResponseDTO;
        } else {
            throw authException;
        }
    }

    private void validateUserAccount(UserModel user) throws AuthException {
        log.info("validating user: {}", user.getUsername());
        validateUserLockStatus(user);
        validateUserActiveStatus(user);
    }

    private void validateUserLockStatus(UserModel user) throws AuthException {
        if (user.getIsLocked()) {
            log.info("user: {} is locked", user.getUsername());
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.USER_ACCOUNT_LOCKED);
        }
    }

    private void validateUserActiveStatus(UserModel user) throws AuthException {
        if (!user.getIsActive()) {
            log.info("user: {} not active", user.getUsername());
            throw new AuthException(HttpStatus.UNAUTHORIZED, ErrorCodes.USER_NOT_ACTIVE);
        }
    }

}
