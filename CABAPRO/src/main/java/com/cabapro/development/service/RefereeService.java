/**
 * Service layer for managing Referee entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Business logic - Referee
 */
package com.cabapro.development.service;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.model.Referee;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefereeService {

    private final RefereeRepository refereeRepository;

    public RefereeService(RefereeRepository refereeRepository) {
        this.refereeRepository = refereeRepository;
    }

    /**
     * Retrieves all referees.
     *
     * @return list of referees
     */
    public List<Referee> findAll() {
        return refereeRepository.findAll();
    }

    /**
     * Finds a referee by its ID.
     *
     * @param id referee ID
     * @return the referee if found
     * @throws IllegalArgumentException if not found
     */
    public Referee findById(Long id) {
        return refereeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found with id: " + id));
    }

    /**
     * Saves a new referee.
     *
     * @param referee referee entity
     * @return saved referee
     */
    public Referee save(Referee referee) {
        if (referee.getId() != null) {
            throw new IllegalArgumentException("New referee should not have an ID");
        }

        if (refereeRepository.existsByEmail(referee.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + referee.getEmail());
        }

        Ranking ranking = referee.getRanking();
        if (ranking == null || ranking.getId() == null) {
            throw new IllegalArgumentException("Ranking must be selected");
        }

        return refereeRepository.save(referee);
    }

    /**
     * Updates a referee.
     *
     * @param id      referee ID
     * @param updated new referee data
     * @return updated referee
     */
    public Referee update(Long id, Referee updated) {
        Referee existing = findById(id);

        if (!existing.getEmail().equals(updated.getEmail()) &&
                refereeRepository.existsByEmail(updated.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + updated.getEmail());
        }

        existing.setEmail(updated.getEmail());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setRanking(updated.getRanking());
        existing.setSpecialty(updated.getSpecialty());

        return refereeRepository.save(existing);
    }

    /**
     * Deletes a referee by its ID.
     *
     * @param id referee ID
     */
    public void deleteById(Long id) {
        if (!refereeRepository.existsById(id)) {
            throw new IllegalArgumentException("Referee not found with id: " + id);
        }
        refereeRepository.deleteById(id);
    }

    /**
     * Searches referees by email.
     *
     * @param query search term
     * @return list of referees that match
     */
    public List<Referee> searchByEmail(String query) {
        return refereeRepository.findByEmailContainingIgnoreCase(query);
    }
}