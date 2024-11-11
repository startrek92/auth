package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiFailureResponse {

    @JsonProperty(value = "error_type")
    private String errorType;

    @JsonProperty(value = "error_code")
    private String errorCode;

    @JsonProperty(value = "error_description")
    private String errorDescription;
}
