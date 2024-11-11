package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginRequest {

    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "password is required")
    private String password;

    @NotNull(message = "state is required")
    private String state;
}
