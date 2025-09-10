package com.cabapro.development.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "tournaments")
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 140)
    private String name;

    @Column(length = 255)
    private String teams;

    @Column(length = 255)
    private String rounds;

    // NEW: base fee configurable por torneo (override al fee del ranking si se define)
    @Column(name = "base_fee", nullable = false)
    private double baseFee = 0.0;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Match> matches = new ArrayList<>();

    // Árbitros asociados al torneo (para restringir asignaciones)
    @ManyToMany
    @JoinTable(
        name = "tournament_referees",
        joinColumns = @JoinColumn(name = "tournament_id"),
        inverseJoinColumns = @JoinColumn(name = "referee_id")
    )
    private Set<Referee> referees = new HashSet<>();

    // --- domain helpers ---
    public List<String> showRounds() {
        if (rounds == null || rounds.isBlank()) return List.of();
        return List.of(rounds.split("\\s*,\\s*"));
    }

    public int refereeCount() { return referees == null ? 0 : referees.size(); }

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeams() { return teams; }
    public void setTeams(String teams) { this.teams = teams; }

    public String getRounds() { return rounds; }
    public void setRounds(String rounds) { this.rounds = rounds; }

    public Double getBaseFee() { return baseFee; }
    public void setBaseFee(Double baseFee) { this.baseFee = baseFee; }

    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }

    public Set<Referee> getReferees() { return referees; }
    public void setReferees(Set<Referee> referees) { this.referees = referees; }
}