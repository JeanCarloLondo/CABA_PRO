package com.cabapro.development.controller;

import com.cabapro.development.model.Match;
import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Tournament;
import com.cabapro.development.repository.AssignmentRepository;
import com.cabapro.development.repository.MatchRepository;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.repository.TournamentRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Admin entrypoint + statistics page.
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

  private final MatchRepository matchRepository;
  private final AssignmentRepository assignmentRepository;
  private final TournamentRepository tournamentRepository;
  private final RefereeRepository refereeRepository;

  public AdminDashboardController(MatchRepository matchRepository,
                                  AssignmentRepository assignmentRepository,
                                  TournamentRepository tournamentRepository,
                                  RefereeRepository refereeRepository) {
    this.matchRepository = matchRepository;
    this.assignmentRepository = assignmentRepository;
    this.tournamentRepository = tournamentRepository;
    this.refereeRepository = refereeRepository;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model,
                          @ModelAttribute("success") String success,
                          @ModelAttribute("error") String error) {
    if (success != null && !success.isBlank()) model.addAttribute("success", success);
    if (error != null && !error.isBlank()) model.addAttribute("error", error);
    return "admin/dashboard";
  }

  @GetMapping("/statistics")
  public String statistics(Model model) {
    // Scheduled vs Completed
    LocalDateTime now = LocalDateTime.now();
    long scheduled = matchRepository.countByMatchDateAfter(now);
    long completed = matchRepository.countByMatchDateBefore(now);
    model.addAttribute("scheduledCount", scheduled);
    model.addAttribute("completedCount", completed);

    // Top-5 referees this month
    YearMonth ym = YearMonth.now();
    LocalDateTime start = ym.atDay(1).atStartOfDay();
    LocalDateTime end = ym.atEndOfMonth().atTime(23, 59, 59);

    var rows = assignmentRepository.topRefereesBetween(start, end, PageRequest.of(0, 5));
    // rows: List<Object[]> -> [0]=refereeId(Long), [1]=matchesCount(Long)

    List<Long> ids = rows.stream().map(r -> (Long) r[0]).toList();
    Map<Long, Referee> refMap = refereeRepository.findAllById(ids).stream()
        .collect(Collectors.toMap(Referee::getId, Function.identity()));

    List<TopRefRow> topReferees = new ArrayList<>();
    for (Object[] r : rows) {
      Long refId = (Long) r[0];
      Long count = (Long) r[1];
      String email = Optional.ofNullable(refMap.get(refId))
          .map(Referee::getEmail)
          .orElse("#" + refId);
      topReferees.add(new TopRefRow(refId, email, count));
    }
    model.addAttribute("topReferees", topReferees);

    // Active tournaments (with future matches)
    // Evitamos usar un método custom inexistente en el repo.
    List<Tournament> activeTournaments = tournamentRepository.findAll().stream()
        .filter(t -> t.getMatches() != null && t.getMatches().stream()
            .map(Match::getMatchDate)
            .filter(Objects::nonNull)
            .anyMatch(d -> d.isAfter(now)))
        .toList();
    model.addAttribute("activeTournaments", activeTournaments);

    return "admin/statistics";
  }

  // DTO for statistics table
  public static class TopRefRow {
    private final Long refereeId;
    private final String email;
    private final Long assignments;

    public TopRefRow(Long refereeId, String email, Long assignments) {
      this.refereeId = refereeId;
      this.email = email;
      this.assignments = assignments;
    }
    public Long getRefereeId() { return refereeId; }
    public String getEmail() { return email; }
    public Long getAssignments() { return assignments; }
  }
}