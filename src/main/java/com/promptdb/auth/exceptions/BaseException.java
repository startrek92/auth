package com.promptdb.auth.exceptions;

import com.promptdb.auth.schemas.ApiResponse;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public abstract class BaseException extends Exception {

    protected final String status = "failure";
    private String errorCode;
    private String errorDescription;
    protected HttpStatus httpStatusCode;

    public BaseException(String errorDescription) {
       super(errorDescription);
   }

   public abstract ApiResponse<?> generateResponse();
}
