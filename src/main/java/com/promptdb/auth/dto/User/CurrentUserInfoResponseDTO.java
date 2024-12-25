package com.promptdb.auth.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptdb.auth.models.CompanyModel;
import com.promptdb.auth.models.UserModel;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CurrentUserInfoResponseDTO {

    @JsonProperty
    private String name;

    @JsonProperty
    private Integer age;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty(value = "company_id")
    private Integer companyId;

    @JsonProperty(value = "company_name")
    private String companyName;

    @JsonProperty(value = "is_active")
    private Boolean isActive;

    @JsonProperty(value = "created_on")
    private Date createdOn;


    public CurrentUserInfoResponseDTO(UserModel user) {

        CompanyModel companyModel = user.getCompanyModel();

        this.name = user.getName();
        this.age = user.getAge();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.companyId = companyModel.getId();
        this.companyName = companyModel.getName();
        this.createdOn = user.getCreatedOn();
        this.isActive = user.getIsActive();
    }
}
