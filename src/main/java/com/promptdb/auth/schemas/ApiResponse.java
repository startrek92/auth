package com.promptdb.auth.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiResponse<T> {

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "data")
    private T data;
}
