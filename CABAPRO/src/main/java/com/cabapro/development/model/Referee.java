/**
 * Represents a referee within the arbitration management system.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Domain entity - Referee
 */
package com.cabapro.development.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Objects;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "referee")
public class Referee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // TODO: relation with User (to be added later)
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ranking_id", nullable = false)
    private Ranking ranking;

    @OneToMany(mappedBy = "referee", cascade = CascadeType.ALL)
    private List<Assignment> assignments = new ArrayList<>();

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    public Referee() {
        // Default constructor required by JPA
    }

    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    public Referee(Ranking ranking, String email, String phoneNumber) {
        this.ranking = ranking;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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