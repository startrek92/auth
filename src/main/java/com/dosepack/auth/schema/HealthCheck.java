package com.dosepack.auth.schema;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HealthCheck {
    String status;
    String commitHash;
}
