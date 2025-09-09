package com.cabapro.development.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_match")
    private Long idMatch;

    @Column(name = "home_team", length = 140)
    private String homeTeam;

    @Column(name = "away_team", length = 140)
    private String awayTeam;

    @Column(name = "location", nullable = false, length = 140)
    private String location;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "match_date", nullable = false)
    private LocalDateTime matchDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tournament_id", nullable = false)
    private Tournament tournament;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments = new ArrayList<>();

    // --- helpers ---
    public int assignedCount() { return assignments == null ? 0 : assignments.size(); }

    // --- getters/setters ---
    public Long getIdMatch() { return idMatch; }
    public void setIdMatch(Long idMatch) { this.idMatch = idMatch; }

    public String getHomeTeam() { return homeTeam; }
    public void setHomeTeam(String homeTeam) { this.homeTeam = homeTeam; }

    public String getAwayTeam() { return awayTeam; }
    public void setAwayTeam(String awayTeam) { this.awayTeam = awayTeam; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getMatchDate() { return matchDate; }
    public void setMatchDate(LocalDateTime matchDate) { this.matchDate = matchDate; }

    public Tournament getTournament() { return tournament; }
    public void setTournament(Tournament tournament) { this.tournament = tournament; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }
}