package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.promptdb.auth.models.CompanyModel;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateCompanyRequest {

    @JsonProperty(value = "name")
    @NotNull(message = "name is required")
    private String name;

    @JsonProperty(value = "is_active")
    private Boolean isActive = true;


    public CompanyModel createCompanyModel() {
        CompanyModel companyModel = new CompanyModel(this.name, this.isActive);
        companyModel.setCreatedOn(new Date());
        companyModel.setModifiedOn(new Date());
        return companyModel;
    }

}
