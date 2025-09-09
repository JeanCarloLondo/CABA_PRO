package com.cabapro.development.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "assignments")
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "referee_id", nullable = false)
    private Referee referee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ranking_id")
    private Ranking ranking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id")
    private Specialty specialty;

    // >>> Relación con ADMIN esperada por AssignmentService / Admin <<<
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AssignmentStatus status = AssignmentStatus.PENDING;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "assignment_fee")
    private BigDecimal assignmentFee;

    // === Getters / Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Match getMatch() { return match; }
    public void setMatch(Match match) { this.match = match; }

    public Referee getReferee() { return referee; }
    public void setReferee(Referee referee) { this.referee = referee; }

    public Ranking getRanking() { return ranking; }
    public void setRanking(Ranking ranking) { this.ranking = ranking; }

    public Specialty getSpecialty() { return specialty; }
    public void setSpecialty(Specialty specialty) { this.specialty = specialty; }

    public Admin getAdmin() { return admin; }
    public void setAdmin(Admin admin) { this.admin = admin; }

    public AssignmentStatus getStatus() { return status; }
    public void setStatus(AssignmentStatus status) { this.status = status; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public BigDecimal getAssignmentFee() { return assignmentFee; }
    public void setAssignmentFee(BigDecimal assignmentFee) { this.assignmentFee = assignmentFee; }
}