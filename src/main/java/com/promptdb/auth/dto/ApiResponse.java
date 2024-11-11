package com.promptdb.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse<T> {

    @JsonProperty(value = "status", defaultValue = "success")
    private String status;

    @JsonProperty(value = "data")
    private T data;
}
