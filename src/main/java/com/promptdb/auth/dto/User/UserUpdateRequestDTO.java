package com.promptdb.auth.dto.User;

import com.promptdb.auth.models.UserModel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class UserUpdateRequestDTO {

    @NotNull(message = "username is required")
    private String username;

    @NotNull(message = "name is required")
    private String name;

    @NotNull(message = "email is required")
    private String email;


    public void updateUserModel(UserModel user) {
        user.setName(this.name);
        user.setUsername(this.username);
        user.setEmail(this.email);
    }

}
