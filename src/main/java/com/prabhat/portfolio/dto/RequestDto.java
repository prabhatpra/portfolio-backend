package com.prabhat.portfolio.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class RequestDto {

	@NotBlank(message = "Name is required")
	@Size(min = 2, max = 50, message = "Name must be between 2 to 50 characters")
	private String name;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	@Size(max = 100, message = "Email too long")
	private String email;
	
	@NotBlank(message = "Subject is required")
	@Size(min = 5, max = 100, message = "subject must be 5 to 100 characters")
	private String subject;
	
	@NotBlank(message = "Message is required")
	@Size(min = 10, max = 1000, message = "Message must be 10-1000 characters")
	private String message;
	
}
