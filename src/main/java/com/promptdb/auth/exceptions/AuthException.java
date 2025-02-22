package com.promptdb.auth.exceptions;

import com.promptdb.auth.dto.ApiFailureResponse;
import com.promptdb.auth.dto.ApiResponse;
import org.springframework.http.HttpStatus;


public class AuthException extends BaseException{

    private final String errorType = AuthException.class.getSimpleName();

    public AuthException(HttpStatus httpStatusCode, ErrorCodes errorCode) {
        super(errorCode.getErrorDescription());
        this.errorCode = errorCode.getErrorCode();
        this.errorDescription = errorCode.getErrorDescription();
        this.httpStatusCode = httpStatusCode;
    }

    @Override
    public ApiResponse<?> generateResponse() {
        ApiFailureResponse apiFailureResponse = new ApiFailureResponse(this.errorType, this.errorCode, this.errorDescription);
        return new ApiResponse<ApiFailureResponse>(this.status, apiFailureResponse);
    }
}
