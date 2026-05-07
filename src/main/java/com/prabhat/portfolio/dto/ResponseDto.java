package com.prabhat.portfolio.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ResponseDto {

    private long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String status;
    private LocalDateTime createdAt;
}