package com.cabapro.development.dto;

public class MailtrapSenderDto {
    private String email;
    private String name;

    // No-argument constructor
    public MailtrapSenderDto() {
    }

    // All-arguments constructor
    public MailtrapSenderDto(String email, String name) {
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
