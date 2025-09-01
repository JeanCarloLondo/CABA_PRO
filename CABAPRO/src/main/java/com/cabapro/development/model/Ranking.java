/**
 * Represents a referee ranking level within the arbitration system.
 *
 * Author: Team #5
 * Date: 2025-08-31
 * Role: Domain entity - ranking
 */

package com.cabapro.development.model;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ranking")
public class Ranking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name; // Example: FIBA, National, Local

    @Column(nullable = false)
    private double baseRate; // Base fee for referees in this ranking

    protected Ranking() {
        // Default constructor required by JPA
    }

    public Ranking(String name, double baseRate) {
        this.name = name;
        this.baseRate = baseRate;
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

    public double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(double baseRate) {
        this.baseRate = baseRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ranking)) return false;
        Ranking that = (Ranking) o;
        return Double.compare(that.baseRate, baseRate) == 0
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, baseRate);
    }
}