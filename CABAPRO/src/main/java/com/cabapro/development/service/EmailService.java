package com.cabapro.development.service;

import com.cabapro.development.dto.MailtrapRecipientDto;
import com.cabapro.development.dto.MailtrapRequestDto;
import com.cabapro.development.dto.MailtrapSenderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Collections;

@Service
public class EmailService {

    @Value("${mailtrap.api.url}")
    private String mailtrapApiUrl;

    @Value("${mailtrap.api.token}")
    private String mailtrapApiToken;

    @Value("${mailtrap.sender.email}")
    private String mailtrapSenderEmail;

    @Value("${mailtrap.sender.name}")
    private String mailtrapSenderName;

    private final RestTemplate restTemplate;
    private final SpringTemplateEngine templateEngine;

    public EmailService(RestTemplate restTemplate, SpringTemplateEngine templateEngine) {
        this.restTemplate = restTemplate;
        this.templateEngine = templateEngine;
    }

    /**
     * Sends a simple email with a raw HTML body.
     */
    public void sendEmail(String toEmail, String subject, String htmlBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(mailtrapApiToken);

        MailtrapSenderDto sender = new MailtrapSenderDto(mailtrapSenderEmail, mailtrapSenderName);
        // The recipient name is optional, so we pass null.
        MailtrapRecipientDto recipient = new MailtrapRecipientDto(toEmail, null);

        MailtrapRequestDto request = new MailtrapRequestDto(
                sender,
                Collections.singletonList(recipient),
                subject,
                htmlBody,
                null // No text body, only HTML
        );

        HttpEntity<MailtrapRequestDto> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForEntity(mailtrapApiUrl, entity, String.class);
            System.out.println("Email sent successfully to " + toEmail);
        } catch (Exception e) {
            System.err.println("Error sending email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sends an email using a Thymeleaf template.
     */
    public void sendTemplatedEmail(String toEmail, String subject, String templateName, Context context) {
        context.setVariable("senderName", mailtrapSenderName);
        String htmlBody = templateEngine.process(templateName, context);
        sendEmail(toEmail, subject, htmlBody);
    }
}
