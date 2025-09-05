/**
 * Repository interface for managing Tournament entities.
 *
 * Author: Alejandro Garcés Ramírez
 * Date: 2025-09-03
 * Role: Data access layer - Tournament
 */
package com.cabapro.development.repository;

import com.cabapro.development.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {
    // Custom query methods (if needed) can be defined here
}