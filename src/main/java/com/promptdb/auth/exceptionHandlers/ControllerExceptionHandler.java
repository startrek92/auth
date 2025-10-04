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
        log.warn("Handled BaseException - status: {}, code: {}, path: {}, message: {}",
                exc.getHttpStatusCode(),
                exc.getErrorCode(),
                req.getDescription(false),
                exc.getMessage()
        );
        log.debug("BaseException stack trace: ", exc);
        return new ResponseEntity<>(exc.generateResponse(), exc.getHttpStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidationErrors(MethodArgumentNotValidException exc) {
        Map<String, String> errors = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        log.warn("Validation failed for request: {} | Total errors: {} | Details: {}",
                exc.getParameter().getExecutable().toGenericString(),
                errors.size(),
                errors
        );

        ApiResponse apiResponse = new ApiResponse<>("failure", errors);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ApiResponse> handleGenericException(Exception exc, WebRequest req) {
        log.error("Unhandled exception occurred at path: {} | Exception: {}",
                req.getDescription(false),
                exc.getMessage(),
                exc
        );

        ApiResponse apiResponse = new ApiResponse<>("failure", "Internal server error occurred");
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}