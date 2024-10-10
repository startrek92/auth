package com.dosepack.auth.services;

import com.dosepack.auth.models.UserModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {
    private List<UserModel> users = new ArrayList<>();

    public AuthService() {
        users.add(new UserModel(UUID.randomUUID().toString(),
                "hello@123",
                "hell123@gmail.com",
                "hello@123"));

        users.add(new UserModel(UUID.randomUUID().toString(),
                "nancyr@123",
                "nancyr@gmail.com",
                "hello@123"));
    }

    public List<UserModel> getUsers() {
        return this.users;
    }
}
