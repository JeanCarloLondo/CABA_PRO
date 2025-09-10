package com.cabapro.development.model;

public enum NotificationType {
    ASSIGNMENT("Nueva Asignación"),
    MATCH_UPDATE("Actualización de Partido"),
    SYSTEM("Notificación del Sistema"),
    REMINDER("Recordatorio"),
    TOURNAMENT_CREATED("Nuevo Torneo"),
    TOURNAMENT_ASSIGNMENT("Asignación a Torneo"),
    TOURNAMENT_REMOVAL("Remoción de Torneo"),
    TOURNAMENT_UPDATE("Actualización de Torneo");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}