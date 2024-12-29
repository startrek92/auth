package com.promptdb.auth.controllers;

import com.promptdb.auth.dto.ApiResponse;
import com.promptdb.auth.dto.User.CurrentUserInfoResponseDTO;
import com.promptdb.auth.dto.User.UserInfoResponseDTO;
import com.promptdb.auth.dto.User.UserUpdateRequestDTO;
import com.promptdb.auth.exceptions.AuthException;
import com.promptdb.auth.models.UserModel;
import com.promptdb.auth.repository.repoInterfaces.UserRepository;
import com.promptdb.auth.services.JWTService;
import com.promptdb.auth.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTService jwtService;

    @Autowired
    UserServices userServices;

    @PutMapping("")
    public UserModel createNewUser(@RequestBody UserModel user) throws AuthException {
        log.info("in user controller, creating new user: {}", user.toString());
        UserModel newUser = userServices.createNewUser(user.getName(), user.getAge());
        return newUser;
    }

    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> getCurrentUserInfo() throws AuthException {
        CurrentUserInfoResponseDTO userModel = userServices.currentUser();
        return new ResponseEntity<>(new ApiResponse(userModel), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ApiResponse> getUserInfoById( @PathVariable Integer id) throws AuthException {
        List<UserInfoResponseDTO> userInfoResponseDTOList = userServices.getUserInfoById(id);

        return new ResponseEntity<>(new ApiResponse(userInfoResponseDTOList), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse> updateCurrentUser(
            @RequestBody UserUpdateRequestDTO userUpdateRequestDTO) throws AuthException {

       UserInfoResponseDTO userInfoResponseDTO = userServices.updateCurrentUser(userUpdateRequestDTO);
       return new ResponseEntity<>(new ApiResponse(userInfoResponseDTO), HttpStatus.OK);

    }
}
