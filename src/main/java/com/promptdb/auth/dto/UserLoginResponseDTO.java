package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptdb.auth.models.UserModel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserLoginResponseDTO {

    @JsonProperty(value = "id")
    Integer id;

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

    public UserLoginResponseDTO(UserModel userModel, String token, String sessionId) {
        this.id = userModel.getId();
        this.username = userModel.getUsername();
        this.name = userModel.getName();
        this.email = userModel.getEmail();
        this.token = token;
        this.sessionId = sessionId;
    }
}
