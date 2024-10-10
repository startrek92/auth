package com.dosepack.auth.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserModel {

    private String id;
    private String username;
    private String email;
    private String password;
}
