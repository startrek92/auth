package com.promptdb.auth.exceptions;

import com.promptdb.auth.dto.ApiFailureResponse;
import com.promptdb.auth.dto.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class AuthExceptionTest {

    @Test
    void generateResponse_buildsFailureApiResponse() {
        AuthException ex = new AuthException(HttpStatus.BAD_REQUEST, ErrorCodes.INVALID_AUTH_HEADER);
        ApiResponse<?> response = ex.generateResponse();

        assertEquals("failure", response.getStatus());
        assertTrue(response.getData() instanceof ApiFailureResponse);

        ApiFailureResponse failure = (ApiFailureResponse) response.getData();
        assertEquals("AuthException", failure.getErrorType());
        assertEquals(ErrorCodes.INVALID_AUTH_HEADER.getErrorCode(), failure.getErrorCode());
        assertEquals(ErrorCodes.INVALID_AUTH_HEADER.getErrorDescription(), failure.getErrorDescription());
    }
}
