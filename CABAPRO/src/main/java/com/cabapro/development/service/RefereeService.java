/**
 * Service layer for managing Referee entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Business logic - Referee
 */
package com.cabapro.development.service;

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
     * Saves or updates a referee.
     *
     * @param referee referee entity
     * @return saved referee
     */
    public Referee save(Referee referee) {
        return refereeRepository.save(referee);
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
}