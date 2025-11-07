package com.cabapro.development.service;

import com.cabapro.development.model.*;
import com.cabapro.development.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepo;
    private final TournamentRepository tournamentRepo;
    private final AssignmentRepository assignmentRepo;
    private final RefereeRepository refereeRepo;
    private final NotificationService notificationService;
    private final EmailService emailService;

    public MatchService(MatchRepository matchRepo,
                        TournamentRepository tournamentRepo,
                        AssignmentRepository assignmentRepo,
                        RefereeRepository refereeRepo,
                        NotificationService notificationService,
                        EmailService emailService) {
        this.matchRepo = matchRepo;
        this.tournamentRepo = tournamentRepo;
        this.assignmentRepo = assignmentRepo;
        this.refereeRepo = refereeRepo;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    // ---- CRUD matches ----
    public List<Match> findAll() { return matchRepo.findAll(); }

    public Match findById(Long id) {
        return matchRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Match not found: " + id));
    }

    @Transactional
    public Match create(Long tournamentId,
                        String location,
                        LocalDateTime date,
                        String homeTeam,
                        String awayTeam) {

        if (tournamentId == null) throw new IllegalArgumentException("Tournament is required");
        if (location == null || location.isBlank()) throw new IllegalArgumentException("Location is required");
        if (date == null) throw new IllegalArgumentException("Date is required");

        Tournament t = tournamentRepo.findById(tournamentId)
                .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + tournamentId));

        Match m = new Match();
        m.setTournament(t);
        m.setLocation(location.trim());
        m.setMatchDate(date);
        m.setHomeTeam(homeTeam);
        m.setAwayTeam(awayTeam);

        return matchRepo.save(m);
    }

    @Transactional
    public Match update(Long id,
                        Long tournamentId,
                        String location,
                        LocalDateTime date,
                        String homeTeam,
                        String awayTeam) {

        Match m = findById(id);

        if (tournamentId != null) {
            Tournament t = tournamentRepo.findById(tournamentId)
                    .orElseThrow(() -> new IllegalArgumentException("Tournament not found: " + tournamentId));
            m.setTournament(t);
        }
        if (location != null && !location.isBlank()) m.setLocation(location.trim());
        if (date != null) m.setMatchDate(date);
        m.setHomeTeam(homeTeam);
        m.setAwayTeam(awayTeam);

        return matchRepo.save(m);
    }

    @Transactional
    public void delete(Long id) {
        matchRepo.deleteById(id);
    }

    // ---- Assignments ----
    public List<Assignment> getAssignments(Long matchId) {
        return assignmentRepo.findByMatch_IdMatch(matchId);
    }

    public List<Referee> eligibleReferees() {
        return refereeRepo.findByRankingIsNotNullAndSpecialtyIsNotNull();
    }

    @Transactional
    public void assignReferee(Long matchId, Long refereeId) {
        Match match = findById(matchId);
        Referee ref = refereeRepo.findById(refereeId)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + refereeId));

        if (ref.getRanking() == null || ref.getSpecialty() == null) {
            throw new IllegalArgumentException("Referee must have ranking and specialty to be assigned.");
        }
        if (assignmentRepo.existsByMatch_IdMatchAndReferee_Id(matchId, refereeId)) {
            throw new IllegalArgumentException("Referee already assigned to this match.");
        }

        Assignment a = new Assignment();
        a.setMatch(match);
        a.setReferee(ref);
        a.setRanking(ref.getRanking());
        a.setSpecialty(ref.getSpecialty());
        a.setStatus(AssignmentStatus.PENDING);
        a.setAssignedAt(LocalDateTime.now());
        a.setAssignmentFee(BigDecimal.valueOf(ref.getRanking().getFee())); // fee desde Ranking

        assignmentRepo.save(a);

        // Notificatión in the app
        notificationService.createAssignmentNotification(ref, match);

        // *** Ibeginin: sent emails after a new assigmet***
        try {
            Context context = new Context();
            // Usamos el email como nombre de reemplazo ya que no hay un campo de nombre
            context.setVariable("refereeName", ref.getEmail());
            context.setVariable("tournamentName", match.getTournament().getName());
            context.setVariable("matchDate", match.getMatchDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            context.setVariable("matchTime", match.getMatchDate().format(DateTimeFormatter.ofPattern("HH:mm")));
            context.setVariable("matchLocation", match.getLocation());

            emailService.sendTemplatedEmail(
                ref.getEmail(),
                "Notificación: Nueva Asignación de Partido",
                "emails/assignment-notification",
                context
            );
        } catch (Exception e) {
            // Log el error para no detener el flujo principal de la asignación
            System.err.println("Failed to send assignment email notification to " + ref.getEmail() + ": " + e.getMessage());
        }
        // *** FIN: Integración de notificación por correo electrónico ***
    }

    @Transactional
    public void unassign(Long assignmentId) {
        assignmentRepo.deleteById(assignmentId);
    }
}