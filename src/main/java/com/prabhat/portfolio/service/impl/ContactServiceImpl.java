package com.prabhat.portfolio.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.prabhat.portfolio.dto.RequestDto;
import com.prabhat.portfolio.dto.ResponseDto;
import com.prabhat.portfolio.entity.Contact;
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

        log.info("POST /contactUser API called for email: {}", request.getEmail());

        String email = request.getEmail();

        // Rate limit check
        if (!rateLimiter.isAllowed(email)) {
            log.warn("Rate limit exceeded for email: {}", email);
            throw new RateLimitException("Too many requests. Try again later.");
        }

        // Duplicate check
        boolean duplicate = repository.existsByEmailAndMessage(email, request.getMessage());

        if (duplicate) {
            log.warn("Duplicate message detected for email: {}", email);
            throw new DuplicateMessageException("Duplicate message not allowed");
        }

        // Hourly limit check
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

        long count = repository.countByEmailAndCreatedAtAfter(email, oneHourAgo);

        if (count >= 3) {
            log.warn("Hourly limit exceeded for email: {}", email);
            throw new RateLimitException("Only 3 messages allowed per hour");
        }

        log.info("Validation passed for email: {}. Saving contact...", email);

        // Save entity
        Contact contact = Contact.builder()
                .name(request.getName())
                .email(email)
                .subject(request.getSubject())
                .message(request.getMessage())
                .status("NEW")
                .build();

        Contact saved = repository.save(contact);

        log.info("Contact saved successfully with id: {}", saved.getId());

        // Send email
        emailService.sendContactMail(
                saved.getName(),
                saved.getEmail(),
                saved.getSubject(),
                saved.getMessage()
        );

        log.info("Email sent successfully to: {}", saved.getEmail());

        // Response
        return ResponseDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .subject(saved.getSubject())
                .message(saved.getMessage())
                .status(saved.getStatus())
                .build();
    }

    @Override
    public List<ResponseDto> getAllContacts() {

        log.info("GET /contacts API called");

        List<ResponseDto> list = repository.findAll()
                .stream()
                .map(c -> ResponseDto.builder()
                        .id(c.getId())
                        .name(c.getName())
                        .email(c.getEmail())
                        .subject(c.getSubject())
                        .message(c.getMessage())
                        .status(c.getStatus())
                        .build()
                )
                .toList();

        log.info("Total contacts fetched: {}", list.size());

        return list;
    }

    @Override
    public ResponseDto getContactById(Long id) {

        log.info("GET /contacts/{} API called", id);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Contact not found with id: {}", id);
                    return new NotFoundException("Contact not found");
                });

        log.info("Contact found with id: {}", id);

        return ResponseDto.builder()
                .id(contact.getId())
                .name(contact.getName())
                .email(contact.getEmail())
                .subject(contact.getSubject())
                .message(contact.getMessage())
                .status(contact.getStatus())
                .build();
    }

    @Override
    public void deleteContact(Long id) {

        log.info("DELETE /contacts/{} API called", id);

        if (!repository.existsById(id)) {
            log.error("Delete failed. Contact not found with id: {}", id);
            throw new NotFoundException("Contact not found");
        }

        repository.deleteById(id);

        log.info("Contact deleted successfully with id: {}", id);
    }

    @Override
    public void updateStatus(Long id, String status) {

        log.info("UPDATE status API called for id: {} with status: {}", id, status);

        Contact contact = repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Update failed. Contact not found with id: {}", id);
                    return new NotFoundException("Contact not found");
                });

        contact.setStatus(status);

        repository.save(contact);

        log.info("Status updated successfully for id: {} to {}", id, status);
    }
}