/**
 * Represents a referee within the arbitration management system.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Domain entity - Referee
 */
package com.cabapro.development.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "referee")
public class Referee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: relation with User (to be added later)
    // TODO: relation with Speciality (to be added later)

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ranking_id", nullable = false)
    private Ranking ranking;

    private String email;

    protected Referee() {
        // Default constructor required by JPA
    }

    public Referee(Ranking ranking) {
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Referee))
            return false;
        Referee referee = (Referee) o;
        return Objects.equals(id, referee.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}