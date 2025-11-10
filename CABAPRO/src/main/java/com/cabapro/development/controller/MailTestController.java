package com.cabapro.development.controller;

import com.cabapro.development.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class MailTestController {

    private final EmailService emailService;

    public MailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send-email")
    public ResponseEntity<String> sendTestEmail(
            @RequestParam String toEmail,
            @RequestParam String subject,
            @RequestParam String htmlBody) {
        try {
            emailService.sendEmail(toEmail, subject, htmlBody);
            return ResponseEntity.ok("Test email sent successfully to " + toEmail);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error sending test email: " + e.getMessage());
        }
    }
}
