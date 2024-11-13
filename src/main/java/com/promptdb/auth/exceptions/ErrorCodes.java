package com.promptdb.auth.exceptions;

public enum ErrorCodes {

    // Authorization Related Errors, prefix: AUTH_
    AUTH_INVALID_CREDENTIALS ("1001", "Invalid Username or Password"),


    // USER Errors, prefix: USER_
    USER_ACCOUNT_LOCKED ("2001", "Account is Locked"),

    INTERNAL_SERVER_ERROR("5001", "Internal Server Error")
    ;

    private final String errorCode;
    private final String errorDescription;

    ErrorCodes(String code, String description) {
        this.errorCode = code;
        this.errorDescription = description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
