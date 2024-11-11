package com.promptdb.auth.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HealthCheckResponse {

    @JsonProperty(value = "status")
    private String status;

    @JsonProperty(value = "commit_hash")
    private String commitHash;
}
