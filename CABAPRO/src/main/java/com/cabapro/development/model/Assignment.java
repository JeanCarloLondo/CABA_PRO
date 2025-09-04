/**
 * Entity representing a referee's assignment to a match.
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Domain entity - Assignment
 */
package com.cabapro.development.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Referee assigned to this match
    @ManyToOne
    @JoinColumn(name = "referee_id", nullable = false)
    private Referee referee;

    // Match associated (assume Match entity will be created)
    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    // Admin who created the assignment
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.PENDING;

    @Column(nullable = false)
    private LocalDateTime assignedAt = LocalDateTime.now();

    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Referee getReferee() {
        return referee;
    }

    public void setReferee(Referee referee) {
        this.referee = referee;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public AssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(AssignmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }
}