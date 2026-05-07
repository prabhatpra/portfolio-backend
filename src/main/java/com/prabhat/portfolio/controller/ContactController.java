package com.prabhat.portfolio.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.prabhat.portfolio.dto.RequestDto;
import com.prabhat.portfolio.dto.ResponseDto;
import com.prabhat.portfolio.service.ContactService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ContactController {

    private final ContactService contactService;

    ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    // ================= POST CONTACT =================
    @PostMapping("/contact")
    public ResponseEntity<ResponseDto> contact(@Valid @RequestBody RequestDto requestDto) {

        log.info("POST /contact API called for email: {}", requestDto.getEmail());

        ResponseDto response = contactService.contactUser(requestDto);

        log.info("POST /contact API completed successfully for email: {}", requestDto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ================= GET ALL =================
    @GetMapping("/contacts")
    public ResponseEntity<List<ResponseDto>> getAllContacts() {

        log.info("GET /contacts API called");

        List<ResponseDto> contacts = contactService.getAllContacts();

        log.info("GET /contacts completed. Total records: {}", contacts.size());

        return ResponseEntity.ok(contacts);
    }

    // ================= GET BY ID =================
    @GetMapping("/contact/{id}")
    public ResponseEntity<ResponseDto> getContactById(@PathVariable Long id) {

        log.info("GET /contact/{} API called", id);

        ResponseDto contact = contactService.getContactById(id);

        log.info("GET /contact/{} completed successfully", id);

        return ResponseEntity.ok(contact);
    }

    // ================= DELETE =================
    @DeleteMapping("/contact/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable Long id) {

        log.info("DELETE /contact/{} API called", id);

        contactService.deleteContact(id);

        log.info("DELETE /contact/{} completed successfully", id);

        return ResponseEntity.ok("Contact deleted successfully");
    }

    // ================= UPDATE STATUS =================
    @PatchMapping("/contact/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable Long id,
                                               @RequestParam String status) {

        log.info("PATCH /contact/{}/status called with status: {}", id, status);

        contactService.updateStatus(id, status);

        log.info("Status updated successfully for id: {}", id);

        return ResponseEntity.ok("Status updated");
    }
}