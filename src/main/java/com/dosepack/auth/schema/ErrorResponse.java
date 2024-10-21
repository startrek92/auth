package com.dosepack.auth.schema;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorResponse {
    Integer errorCode;
    String errorMessage;
}
