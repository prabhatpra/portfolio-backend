package com.prabhat.portfolio.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prabhat.portfolio.entity.Contact;
import com.prabhat.portfolio.enums.ContactStatus;

public interface ContactRepository extends JpaRepository<Contact, Long> {

	
	
	boolean existsByEmailAndMessage(
			
			String email,
			String message
			);
	
	
	
	long countByEmailAndCreatedAtAfter(
			String email,
			LocalDateTime time
			);
	
	
	List<Contact> findByEmailOrderByCreatedAtDesc(
			String email
			);
	
	List<Contact> findByStatus(ContactStatus status);
	

	
	}
