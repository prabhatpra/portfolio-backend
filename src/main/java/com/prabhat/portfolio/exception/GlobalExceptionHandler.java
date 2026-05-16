package com.prabhat.portfolio.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prabhat.portfolio.exception.ContactException.DuplicateMessageException;
import com.prabhat.portfolio.exception.ContactException.InvalidStatusException;
import com.prabhat.portfolio.exception.ContactException.NotFoundException;
import com.prabhat.portfolio.exception.ContactException.RateLimitException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    
    private ResponseEntity<Map<String, Object>> buildResponse(
            String message,
            HttpStatus status
    ) {

        Map<String, Object> error = new HashMap<>();

        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.name());
        error.put("message", message);

        return new ResponseEntity<>(error, status);
    }

  
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimit(
            RateLimitException ex) {

        log.warn("RateLimitException: {}", ex.getMessage());

        return buildResponse(
                ex.getMessage(),
                HttpStatus.TOO_MANY_REQUESTS
        );
    }

    
    @ExceptionHandler(DuplicateMessageException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(
            DuplicateMessageException ex) {

        log.warn("DuplicateMessageException: {}", ex.getMessage());

        return buildResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            NotFoundException ex) {

        log.error("NotFoundException: {}", ex.getMessage());

        return buildResponse(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    
    @ExceptionHandler(InvalidStatusException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStatus(
            InvalidStatusException ex) {

        log.warn("InvalidStatusException: {}", ex.getMessage());

        return buildResponse(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST
        );
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> validationErrors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        ));

        Map<String, Object> response = new HashMap<>();

        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Validation Failed");
        response.put("messages", validationErrors);

        log.warn("Validation failed: {}", validationErrors);

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

   
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        log.warn("IllegalArgumentException: {}", ex.getMessage());

        return buildResponse(
                "Invalid request value",
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResource(
            org.springframework.web.servlet.resource.NoResourceFoundException ex) {
        log.debug("No static resource: {}", ex.getMessage());
        return ResponseEntity.notFound().build();
    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(
            Exception ex) {

        log.error("Unexpected error occurred: ", ex);

        return buildResponse(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    
}