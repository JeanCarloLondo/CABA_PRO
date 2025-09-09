/**
 * Repository interface for managing Referee entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Data access layer - Referee
 */

package com.cabapro.development.repository;

import com.cabapro.development.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    /**
     * Checks if a referee exists with the given email.
     *
     * @param email the referee's email
     * @return true if exists, false otherwise
     */
    boolean existsByEmail(String email);

    /**
     * Searches referees by email, ignoring case.
     *
     * @param email part of the referee's email
     * @return list of referees that match the query
     */
    List<Referee> findByEmailContainingIgnoreCase(String email);
}