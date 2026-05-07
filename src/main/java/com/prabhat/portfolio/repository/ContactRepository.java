package com.prabhat.portfolio.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhat.portfolio.entity.Contact;

public interface ContactRepository extends JpaRepository<Contact, Long> {

	
	// duplicate message check 
	boolean existsByEmailAndMessage(
			
			String email,
			String message
			);
	// rate limit (count message in time range)
	
	
	long countByEmailAndCreatedAtAfter(
			String email,
			LocalDateTime time
			);
	
	// user message history (latest first)
	List<Contact> findByEmailOrderByCreatedAtDesc(
			String email
			);
	
	List<Contact> findByStatus(String status);
	

	
	}
