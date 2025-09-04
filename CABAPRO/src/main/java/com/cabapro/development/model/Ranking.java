/**
 * Represents a referee ranking level within the arbitration system.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Domain entity - ranking
 */

package com.cabapro.development.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // Example: FIBA, National, Local

    @Column(nullable = false)
    private double fee; // Payment fee for this ranking

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "ranking", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Referee> referees = new ArrayList<>();

    public Ranking() {
        // Default constructor required by JPA
    }

    public Ranking(String name, double fee, String description) {
        this.name = name;
        this.fee = fee;
        this.description = description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public void setReferees(List<Referee> referees) {
        this.referees = referees;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Ranking))
            return false;
        Ranking ranking = (Ranking) o;
        return Double.compare(ranking.fee, fee) == 0
                && Objects.equals(id, ranking.id)
                && Objects.equals(name, ranking.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, fee);
    }
}