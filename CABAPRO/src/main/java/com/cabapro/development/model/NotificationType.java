package com.cabapro.development.model;

public enum NotificationType {
    ASSIGNMENT("New Assignment"),
    MATCH_UPDATE("Match Update"),
    SYSTEM("System Notification"),
    REMINDER("Reminder");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}