/**
 * Repository interface for managing Referee entities.
 *
 * Author: Team #5
 * Date: 2025-08-31
 * Role: Data access layer - Referee
 */

package com.cabapro.development.repository;

import com.cabapro.development.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {
    /**
     * Finds a referee by email.
     *
     * @param email the referee's email
     * @return an Optional containing the Referee if found, otherwise empty
     */
    Optional<Referee> findByEmail(String email);
}