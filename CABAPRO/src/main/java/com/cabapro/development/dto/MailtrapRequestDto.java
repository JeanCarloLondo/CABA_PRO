package com.cabapro.development.dto;

import java.util.List;

public class MailtrapRequestDto {
    private MailtrapSenderDto from;
    private List<MailtrapRecipientDto> to;
    private String subject;
    private String html;
    private String text;

    // No-argument constructor
    public MailtrapRequestDto() {
    }

    // All-arguments constructor
    public MailtrapRequestDto(MailtrapSenderDto from, List<MailtrapRecipientDto> to, String subject, String html, String text) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.html = html;
        this.text = text;
    }

    // Getters and Setters
    public MailtrapSenderDto getFrom() {
        return from;
    }

    public void setFrom(MailtrapSenderDto from) {
        this.from = from;
    }

    public List<MailtrapRecipientDto> getTo() {
        return to;
    }

    public void setTo(List<MailtrapRecipientDto> to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
