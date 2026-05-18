package com.prabhat.portfolio.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prabhat.portfolio.dto.RequestDto;
import com.prabhat.portfolio.dto.ResponseDto;
import com.prabhat.portfolio.entity.Contact;
import com.prabhat.portfolio.enums.ContactStatus;
import com.prabhat.portfolio.exception.ContactException.DuplicateMessageException;
import com.prabhat.portfolio.exception.ContactException.NotFoundException;
import com.prabhat.portfolio.exception.ContactException.RateLimitException;
import com.prabhat.portfolio.repository.ContactRepository;
import com.prabhat.portfolio.service.ContactService;
import com.prabhat.portfolio.service.EmailService;
import com.prabhat.portfolio.util.RateLimiter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    private final ContactRepository repository;
    private final RateLimiter rateLimiter;
    private final EmailService emailService;

    public ContactServiceImpl(ContactRepository repository,
                              RateLimiter rateLimiter,
                              EmailService emailService) {
        this.repository = repository;
        this.rateLimiter = rateLimiter;
        this.emailService = emailService;
    }

    @Override
    public ResponseDto contactUser(RequestDto request) {

        String email = request.getEmail();

        log.info("Contact request received for email: {}", email);

        // 1. Rate limit check
        if (!rateLimiter.isAllowed(email)) {
            log.warn("Rate limit exceeded for email: {}", email);
            throw new RateLimitException("Too many requests. Try again later.");
        }

        // 2. Duplicate message check
        boolean duplicate = repository.existsByEmailAndMessage(email, request.getMessage());

        if (duplicate) {
            log.warn("Duplicate message detected for email: {}", email);
            throw new DuplicateMessageException("Duplicate message not allowed");
        }

        // 3. Hourly limit check
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        long count = repository.countByEmailAndCreatedAtAfter(email, oneHourAgo);

        if (count >= 3) {
            log.warn("Hourly limit exceeded for email: {}", email);
            throw new RateLimitException("Only 3 messages allowed per hour");
        }

        log.info("Validation passed for email: {}", email);

        // 4. Save contact
        Contact contact = Contact.builder()
                .name(request.getName())
                .email(email)
                .subject(request.getSubject())
                .message(request.getMessage())
                .status(ContactStatus.NEW)
                .build();

        Contact saved = repository.save(contact);

        log.info("Contact saved successfully with id: {}", saved.getId());

        // 5. Send admin email
        emailService.sendContactMail(
                saved.getName(),
                saved.getEmail(),
                saved.getSubject(),
                saved.getMessage()
        );

        log.info("Admin notification email triggered for: {}", email);


        log.info("Contact processing completed successfully for: {}", email);

        
        return ResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .subject(saved.getSubject())
                .message(saved.getMessage())
                .status(saved.getStatus())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    @Override
    public List<ResponseDto> getAllContacts() {

        log.info("Fetching all contacts");

        List<ResponseDto> list = repository.findAll()
                .stream()
                .map(c -> ResponseDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .email(c.getEmail())
                        .subject(c.getSubject())
                        .message(c.getMessage())
                        .status(c.getStatus())
                        .createdAt(c.getCreatedAt())
                        .build())
                .toList();

        log.info("Total contacts fetched: {}", list.size());

        return list;
    }

    @Override
    public ResponseDto getContactById(Long id) {

        log.info("Fetching contact by id: {}", id);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found with id: {}", id);
                    return new NotFoundException("Contact not found");
                });

        return ResponseDto.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .createdAt(contact.getCreatedAt())
                .build();
    }

    @Override
    public void deleteContact(Long id) {

        log.info("Deleting contact id: {}", id);

        if (!repository.existsById(id)) {
            log.error("Contact not found for delete: {}", id);
            throw new NotFoundException("Contact not found");
        }

        repository.deleteById(id);

        log.info("Contact deleted successfully: {}", id);
    }

    @Override
    public void updateStatus(Long id, ContactStatus status) {

        log.info("Updating status for id: {} to {}", id, status);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found for update: {}", id);
                    return new NotFoundException("Contact not found");
                });

        contact.setStatus(status);

        repository.save(contact);

        log.info("Status updated successfully for id: {}", id);
    }
}