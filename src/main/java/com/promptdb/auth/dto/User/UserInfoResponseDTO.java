package com.promptdb.auth.dto.User;

import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;

public class UserInfoResponseDTO extends CurrentUserInfoResponseDTO {



    public UserInfoResponseDTO(UserModel user) {
       super(user);
    }
}
