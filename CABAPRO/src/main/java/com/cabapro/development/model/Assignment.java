/**
 * Entity representing a referee's assignment to a match.
 *
 * Author: Jean Londoño - Alejandro Garcés Ramírez
 * Date: 2025-09-03
 * Role: Domain entity - Assignment
 */
package com.cabapro.development.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

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

    // Match associated
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

    // Specialty of the referee in this assignment (COURT or TABLE)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Specialty specialty;

    // Ranking/level of the referee at the time of assignment
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ranking_id", nullable = false)
    private Ranking level;

    // Fee assigned for this match
    @Column(name = "assignment_fee", nullable = false, precision = 12, scale = 2)
    private BigDecimal assignmentFee = BigDecimal.ZERO;

    // --- Domain methods ---
    public void accepted() {
        this.status = AssignmentStatus.ACCEPTED;
    }

    public void rejected() {
        this.status = AssignmentStatus.REJECTED;
    }

    public BigDecimal calculatePayment() {
        return assignmentFee;
    }

    public String showDetails() {
        return "Assignment ID=" + id
                + " | Match #" + (match != null ? match.getId() : "N/A")
                + " | Referee #" + (referee != null ? referee.getId() : "N/A")
                + " | Specialty=" + specialty
                + " | Level=" + (level != null ? level.getName() : "N/A")
                + " | Fee=" + assignmentFee
                + " | Status=" + status;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Referee getReferee() { return referee; }
    public void setReferee(Referee referee) { this.referee = referee; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public Specialty getSpecialty() { return specialty; }
    public void setSpecialty(Specialty specialty) { this.specialty = specialty; }

    public Ranking getLevel() { return level; }
    public void setLevel(Ranking level) { this.level = level; }

    public BigDecimal getAssignmentFee() { return assignmentFee; }
    public void setAssignmentFee(BigDecimal assignmentFee) { this.assignmentFee = assignmentFee; }
}
