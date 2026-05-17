package com.prabhat.portfolio.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.prabhat.portfolio.constant.EmailConstants;
import com.prabhat.portfolio.service.EmailService;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    // ---------------- ADMIN EMAIL ----------------
    @Async
    @Override
    public void sendContactMail(String name, String email, String subject, String message) {
        log.info("Preparing admin email for user: {}", email);
        try {
            Resend resend = new Resend(resendApiKey);
            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("onboarding@resend.dev")
                    .to(EmailConstants.ADMIN_EMAIL)
                    .replyTo(email)
                    .subject("Portfolio Contact: " + subject + " (" + email + ")")
                    .text(buildAdminMessage(name, email, subject, message))
                    .build();
            resend.emails().send(params);
            log.info("Admin email sent successfully for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to send admin email for user: {}", email, e);
        }
    }

    // ---------------- AUTO REPLY ----------------
    @Async
    @Override
    public void sendAutoReply(String name, String email) {
        log.info("Auto reply skipped (domain not verified): {}", email);
        // TODO: Enable when domain is verified
    }

    // ---------------- HELPERS ----------------
    private String buildAdminMessage(String name, String email, String subject, String message) {
        return new StringBuilder()
                .append("Name: ").append(name).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Subject: ").append(subject).append("\n")
                .append("Message: ").append(message)
                .toString();
    }
}