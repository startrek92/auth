package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginResponseDTO {

    @JsonProperty(value = "username")
    String username;

    @JsonProperty(value = "name")
    String name;

    @JsonProperty(value = "email")
    String email;

    @JsonProperty(value = "token")
    String token;

    @JsonIgnore
    String sessionId;
}
