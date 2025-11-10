/**
 * Service for managing notifications in the CABAPRO system.
 *
 * Author: Alejandra Ortiz / Claude Assistant
 * Date: 2025-09-10
 * Role: Business logic for notifications
 */
package com.cabapro.development.service;

import com.cabapro.development.model.*;
import com.cabapro.development.repository.NotificationRepository;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final RefereeRepository refereeRepository;

    public NotificationService(NotificationRepository notificationRepository, 
                             RefereeRepository refereeRepository) {
        this.notificationRepository = notificationRepository;
        this.refereeRepository = refereeRepository;
    }

    @Transactional
    public void createAssignmentNotification(Referee referee, Match match) {
        String title = "Nueva Asignación de Partido";
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String matchInfo = (match.getHomeTeam() != null && match.getAwayTeam() != null) 
            ? match.getHomeTeam() + " vs " + match.getAwayTeam() 
            : "Partido en " + match.getLocation();
            
        String message = String.format(
            "Has sido asignado al partido: %s. Fecha: %s. Ubicación: %s",
            matchInfo,
            match.getMatchDate().format(formatter),
            match.getLocation()
        );

        Notification notification = new Notification(
            referee, 
            title, 
            message, 
            NotificationType.ASSIGNMENT, 
            match
        );

        notificationRepository.save(notification);
    }

    /**
     * Crear notificación cuando se crea un nuevo torneo
     */
    @Transactional
    public void createTournamentCreatedNotification(Tournament tournament) {
        String title = "Nuevo Torneo Creado";
        String message = String.format(
            "Se ha creado un nuevo torneo: %s. Mantente atento para posibles asignaciones.",
            tournament.getName()
        );

        // Obtener todos los referees activos
        List<Referee> allReferees = refereeRepository.findAll();
        
        // Crear notificación para cada referee
        for (Referee referee : allReferees) {
            Notification notification = new Notification(
                referee, 
                title, 
                message, 
                NotificationType.SYSTEM
            );
            notificationRepository.save(notification);
        }
    }

    /**
     * Crear notificación cuando un referee es asignado a un torneo
     */
    @Transactional
    public void createTournamentAssignmentNotification(Referee referee, Tournament tournament) {
        String title = "Asignado a Torneo";
        String message = String.format(
            "Has sido asignado al torneo: %s. Ya puedes recibir asignaciones de partidos para este torneo.",
            tournament.getName()
        );

        Notification notification = new Notification(
            referee, 
            title, 
            message, 
            NotificationType.ASSIGNMENT
        );

        notificationRepository.save(notification);
    }

    /**
     * Crear notificación cuando un referee es removido de un torneo
     */
    @Transactional
    public void createTournamentRemovalNotification(Referee referee, Tournament tournament) {
        String title = "Removido del Torneo";
        String message = String.format(
            "Has sido removido del torneo: %s. Ya no recibirás asignaciones para este torneo.",
            tournament.getName()
        );

        Notification notification = new Notification(
            referee, 
            title, 
            message, 
            NotificationType.SYSTEM
        );

        notificationRepository.save(notification);
    }

    /**
     * Crear notificación general para un referee específico
     */
    @Transactional
    public void createNotification(Referee referee, String title, String message, NotificationType type) {
        Notification notification = new Notification(referee, title, message, type);
        notificationRepository.save(notification);
    }

    /**
     * Crear notificación masiva para todos los referees
     */
    @Transactional
    public void createBulkNotification(String title, String message, NotificationType type) {
        List<Referee> allReferees = refereeRepository.findAll();
        
        for (Referee referee : allReferees) {
            Notification notification = new Notification(referee, title, message, type);
            notificationRepository.save(notification);
        }
    }

    public List<Notification> getNotificationsByReferee(Long refereeId) {
        return notificationRepository.findByReferee_IdOrderByCreatedAtDesc(refereeId);
    }

    public List<Notification> getUnreadNotificationsByReferee(Long refereeId) {
        return notificationRepository.findByReferee_IdAndIsReadFalseOrderByCreatedAtDesc(refereeId);
    }

    public long countUnreadNotifications(Long refereeId) {
        return notificationRepository.countByReferee_IdAndIsReadFalse(refereeId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.markAsRead(notificationId);
    }

    @Transactional
    public void markAllAsRead(Long refereeId) {
        notificationRepository.markAllAsReadByRefereeId(refereeId);
    }

    public Notification findById(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Notification not found: " + id));
    }

    @Transactional
    public void cleanupOldNotifications() {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteOldNotifications(cutoffDate);
    }

    public List<Notification> getRecentNotifications(Long refereeId) {
        LocalDateTime since = LocalDateTime.now().minusDays(7);
        return notificationRepository.findByReferee_IdAndCreatedAtAfterOrderByCreatedAtDesc(refereeId, since);
    }
}