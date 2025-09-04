/**
 * Entity representing a Tournament of the system.
 *
 * Author: Alejandro Garcés Ramírez
 * Date: 2025-09-03
 * Role: Domain entity - Tournament
 */

package com.cabapro.development.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Match> matches = new ArrayList<>();

    // --- domain helper ---
    public List<String> showRounds() {
        if (rounds == null || rounds.isBlank()) return List.of();
        return List.of(rounds.split("\\s*,\\s*"));
    }

    // --- getters/setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTeams() { return teams; }
    public void setTeams(String teams) { this.teams = teams; }

    public String getRounds() { return rounds; }
    public void setRounds(String rounds) { this.rounds = rounds; }

    public List<Match> getMatches() { return matches; }
    public void setMatches(List<Match> matches) { this.matches = matches; }
}