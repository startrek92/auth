package com.promptdb.auth.exceptionHandlers;

import com.promptdb.auth.exceptions.BaseException;
import com.promptdb.auth.schemas.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse>handleExceptions(BaseException exc, WebRequest req) {
        return new ResponseEntity<ApiResponse>(exc.generateResponse(), exc.getHttpStatusCode());
    }
}
