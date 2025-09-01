/**
 * Represents a referee within the arbitration management system.
 *
 * Author: Team #5
 * Date: 2025-08-31
 * Role: Domain entity - Arbitro
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

    @Column(nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(nullable = false)
    private String specialty; // Example: Court, Table

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id", nullable = false)
    private Ranking ranking;

    protected Referee() {
        // Default constructor required by JPA
    }

    public Referee(String fullName, String email, String phone, String specialty, Ranking ranking) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.specialty = specialty;
        this.ranking = ranking;
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking ranking) {
        this.ranking = ranking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Referee)) return false;
        Referee referee = (Referee) o;
        return Objects.equals(id, referee.id)
                && Objects.equals(email, referee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}