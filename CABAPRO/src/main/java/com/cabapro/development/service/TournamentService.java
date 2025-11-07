package com.cabapro.development.service;

import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Tournament;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TournamentService {

    private final TournamentRepository tournamentRepo;
    private final RefereeRepository refereeRepo;
    private final NotificationService notificationService;

    public TournamentService(TournamentRepository tournamentRepo,
                             RefereeRepository refereeRepo,
                             NotificationService notificationService) {
        this.tournamentRepo = tournamentRepo;
        this.refereeRepo = refereeRepo;
        this.notificationService = notificationService;
    }

    public List<Tournament> findAll() {
        return tournamentRepo.findAll();
    }

    public Tournament findById(Long id) {
        return tournamentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + id));
    }

    @Transactional
    public Tournament create(String name, String teams, String rounds) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (tournamentRepo.existsByNameIgnoreCase(name.trim())) {
            throw new IllegalArgumentException("Tournament name already exists: " + name);
        }
        
        Tournament t = new Tournament();
        t.setName(name.trim());
        t.setTeams(teams);
        t.setRounds(rounds);
        
        // Guardar el torneo
        Tournament savedTournament = tournamentRepo.save(t);
        
        // Crear notificaciones para todos los referees
        try {
            notificationService.createTournamentCreatedNotification(savedTournament);
        } catch (Exception e) {
            // Log el error pero no fallar la creación del torneo
            System.err.println("Error creating tournament notifications: " + e.getMessage());
        }
        
        return savedTournament;
    }

    @Transactional
    public Tournament update(Long id, String name, String teams, String rounds) {
        Tournament t = findById(id);
        String newName = name == null ? "" : name.trim();
        if (newName.isBlank()) {
            throw new IllegalArgumentException("Name is required");
        }
        if (!t.getName().equalsIgnoreCase(newName) && tournamentRepo.existsByNameIgnoreCase(newName)) {
            throw new IllegalArgumentException("Tournament name already exists: " + newName);
        }
        
        // Verificar si cambió el nombre para enviar notificación
        boolean nameChanged = !t.getName().equals(newName);
        
        t.setName(newName);
        t.setTeams(teams);
        t.setRounds(rounds);
        
        Tournament updatedTournament = tournamentRepo.save(t);
        
        // Si cambió información importante, notificar a los referees asignados
        if (nameChanged || (teams != null && !teams.equals(t.getTeams()))) {
            try {
                for (Referee referee : t.getReferees()) {
                    notificationService.createNotification(
                        referee,
                        "Torneo Actualizado",
                        String.format("El torneo %s ha sido actualizado. Revisa los detalles.", newName),
                        com.cabapro.development.model.NotificationType.SYSTEM
                    );
                }
            } catch (Exception e) {
                System.err.println("Error creating tournament update notifications: " + e.getMessage());
            }
        }
        
        return updatedTournament;
    }

    @Transactional
    public void delete(Long id) {
        Tournament t = findById(id);
        
        // Notificar a los referees asignados antes de eliminar
        try {
            for (Referee referee : t.getReferees()) {
                notificationService.createNotification(
                    referee,
                    "Torneo Eliminado",
                    String.format("El torneo %s ha sido eliminado.", t.getName()),
                    com.cabapro.development.model.NotificationType.SYSTEM
                );
            }
        } catch (Exception e) {
            System.err.println("Error creating tournament deletion notifications: " + e.getMessage());
        }
        
        // regla: puedes impedir borrar si tiene partidos
        // if (!t.getMatches().isEmpty()) throw new IllegalArgumentException("Cannot delete: tournament has matches");
        tournamentRepo.delete(t);
    }

    // ---------- Asignación de árbitros ----------
    public Set<Referee> assignedReferees(Long tournamentId) {
        return new HashSet<>(findById(tournamentId).getReferees());
    }

    public List<Referee> unassignedReferees(Long tournamentId) {
        Set<Long> assignedIds = assignedReferees(tournamentId).stream()
                .map(Referee::getId)
                .collect(Collectors.toSet());
        return refereeRepo.findAll().stream()
                .filter(r -> !assignedIds.contains(r.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public void addReferee(Long tournamentId, Long refereeId) {
        Tournament t = findById(tournamentId);
        Referee r = refereeRepo.findById(refereeId)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + refereeId));
        
        // Verificar si ya está asignado
        if (t.getReferees().contains(r)) {
            throw new IllegalArgumentException("Referee is already assigned to this tournament");
        }
        
        t.getReferees().add(r);
        tournamentRepo.save(t);
        
        // Enviar notificación al referee
        try {
            notificationService.createTournamentAssignmentNotification(r, t);
        } catch (Exception e) {
            System.err.println("Error creating referee assignment notification: " + e.getMessage());
        }
    }

    @Transactional
    public void removeReferee(Long tournamentId, Long refereeId) {
        Tournament t = findById(tournamentId);
        Referee referee = refereeRepo.findById(refereeId)
                .orElse(null);
        
        boolean wasRemoved = t.getReferees().removeIf(r -> Objects.equals(r.getId(), refereeId));
        
        if (wasRemoved) {
            tournamentRepo.save(t);
            
            // Enviar notificación al referee si se encontró
            if (referee != null) {
                try {
                    notificationService.createTournamentRemovalNotification(referee, t);
                } catch (Exception e) {
                    System.err.println("Error creating referee removal notification: " + e.getMessage());
                }
            }
        }
    }

    public List<Tournament> findActive(LocalDateTime since) {
        return tournamentRepo.findActiveSince(since);
    }

    /**
     * Método auxiliar para enviar notificaciones masivas a todos los referees de un torneo
     */
    @Transactional
    public void notifyTournamentReferees(Long tournamentId, String title, String message) {
        Tournament t = findById(tournamentId);
        
        for (Referee referee : t.getReferees()) {
            try {
                notificationService.createNotification(
                    referee,
                    title,
                    message,
                    com.cabapro.development.model.NotificationType.SYSTEM
                );
            } catch (Exception e) {
                System.err.println("Error notifying referee " + referee.getId() + ": " + e.getMessage());
            }
        }
    }
}