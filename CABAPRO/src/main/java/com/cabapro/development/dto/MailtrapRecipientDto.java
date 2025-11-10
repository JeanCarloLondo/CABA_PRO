package com.cabapro.development.dto;

public class MailtrapRecipientDto {
    private String email;
    private String name;

    // No-argument constructor
    public MailtrapRecipientDto() {
    }

    // All-arguments constructor
    public MailtrapRecipientDto(String email, String name) {
        this.email = email;
        this.name = name;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
