package com.prabhat.portfolio.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    private String message;
    private int status;
    private String error;
    private String path;
    private LocalDateTime timestamp;

    public static ApiError of(String message, HttpStatus status, String path) {
        return ApiError.builder()
                .message(message)
                .status(status.value())
                .error(status.name())
                .path(path)
                .timestamp(LocalDateTime.now())
                .build();
    }
}