package com.promptdb.auth.schemas;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginRequest {
    private String username;
    private String password;
    private String state;
}
