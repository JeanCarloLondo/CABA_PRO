/**
 * Enum representing the specialty of a referee.
 * Indicates whether the referee works on the court or at the table.
 *
 * Author: Alejandro Garcés Ramírez - Alejandra Ortiz
 * Date: 2025-09-04
 * Role: Domain enum - Specialty
 */
package com.cabapro.development.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "specialties")
public class Specialty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSpecialty;

    private String name;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assignment> assignments;

    // --- Domain Methods ---
    public boolean isCompatibleMatch(Match match) {
        return match != null && match.getLocation() != null;
    }

    public String showInfo() {
        return name + ": " + description;
    }

    // --- Getters y Setters ---
    public Long getIdSpecialty() { return idSpecialty; }
    public void setIdSpecialty(Long idSpecialty) { this.idSpecialty = idSpecialty; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<Assignment> getAssignments() { return assignments; }
    public void setAssignments(List<Assignment> assignments) { this.assignments = assignments; }
}