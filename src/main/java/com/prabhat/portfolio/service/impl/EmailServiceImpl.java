package com.prabhat.portfolio.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.prabhat.portfolio.constant.EmailConstants;
import com.prabhat.portfolio.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    // ---------------- ADMIN EMAIL ----------------
    @Async
    @Override
    public void sendContactMail(String name, String email, String subject, String message) {

        log.info("Preparing admin email for user: {}", email);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(EmailConstants.ADMIN_EMAIL);
            mail.setFrom(EmailConstants.ADMIN_EMAIL);
            mail.setReplyTo(email);
            mail.setSubject(buildAdminSubject(subject, email));
            mail.setText(buildAdminMessage(name, email, message));

            mailSender.send(mail);

            log.info("Admin email sent successfully for user: {}", email);

        } catch (Exception e) {
            log.error("Failed to send admin email for user: {}", email, e);
        }
    }

    // ---------------- AUTO REPLY ----------------
    @Async
    @Override
    public void sendAutoReply(String name, String email) {

        log.info("Preparing auto reply for user: {}", email);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo(email);
            mail.setFrom(EmailConstants.ADMIN_EMAIL);
            mail.setSubject(EmailConstants.THANK_YOU_SUBJECT);
            mail.setText(buildAutoReplyMessage(name));

            mailSender.send(mail);

            log.info("Auto reply sent successfully to user: {}", email);

        } catch (Exception e) {
            log.error("Auto reply failed for user: {}", email, e);
        }
    }

    // ---------------- HELPERS ----------------

    private String buildAdminSubject(String subject, String email) {
        return "Portfolio Contact: " + subject + " (" + email + ")";
    }

    private String buildAdminMessage(String name, String email, String message) {
        return new StringBuilder()
                .append("Name: ").append(name).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Message: ").append(message)
                .toString();
    }

    private String buildAutoReplyMessage(String name) {
        return new StringBuilder()
                .append("Hi ").append(name).append(",\n\n")
                .append("Thank you for contacting us.\n")
                .append("We will get back to you within a short time.\n\n")
                .append("Regards,\nAdmin Team")
                .toString();
    }
}