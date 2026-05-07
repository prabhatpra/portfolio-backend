package com.prabhat.portfolio.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.prabhat.portfolio.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendContactMail(String name, String email, String subject, String message) {

        log.info("Email sending started to admin for user email: {}", email);

        try {
            SimpleMailMessage mail = new SimpleMailMessage();

            mail.setTo("prabhatprajapati01@gmail.com");
            mail.setFrom("prabhatprajapati01@gmail.com");
            mail.setReplyTo(email);

            String finalSubject = "Portfolio Contact: " + subject + " (" + email + ")";
            mail.setSubject(finalSubject);

            String finalMessage =
                    "Name: " + name + "\n" +
                    "Email: " + email + "\n" +
                    "Message: " + message;

            mail.setText(finalMessage);

            log.info("Mail prepared successfully for email: {}", email);

            mailSender.send(mail);

            log.info("Email sent successfully to admin. From user: {}", email);

        } catch (Exception e) {

            log.error("Failed to send email for user: {}. Error: {}", email, e.getMessage(), e);

        }
    }
}