package com.promptdb.auth.exceptionHandlers;

import com.promptdb.auth.exceptions.BaseException;
import com.promptdb.auth.dto.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiResponse> handleExceptions(BaseException exc, WebRequest req) {
        return new ResponseEntity<ApiResponse>(exc.generateResponse(), exc.getHttpStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException exc) {
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        ApiResponse apiResponse = new ApiResponse<>("failure", errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}
