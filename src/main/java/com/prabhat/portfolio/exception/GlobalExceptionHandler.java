package com.prabhat.portfolio.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prabhat.portfolio.exception.ContactException.DuplicateMessageException;
import com.prabhat.portfolio.exception.ContactException.RateLimitException;
import com.prabhat.portfolio.exception.ContactException.NotFoundException;

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

    // 1. Rate limit
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<Map<String, Object>> handleRateLimit(RateLimitException ex) {

        log.warn("RateLimitException: {}", ex.getMessage());

        return buildResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS);
    }

    // 2. Duplicate message
    @ExceptionHandler(DuplicateMessageException.class)
    public ResponseEntity<Map<String, Object>> handleDuplicate(DuplicateMessageException ex) {

        log.warn("DuplicateMessageException: {}", ex.getMessage());

        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 3. Not found
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(NotFoundException ex) {

        log.error("NotFoundException: {}", ex.getMessage());

        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 4. Generic error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex) {

        log.error("Unexpected error occurred: ", ex);

        return buildResponse(
                "Something went wrong",
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}