/**
 * Service layer for managing Ranking entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Business logic - Ranking
 */
package com.cabapro.development.service;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.repository.RankingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankingService {

    private final RankingRepository rankingRepository;

    public RankingService(RankingRepository rankingRepository) {
        this.rankingRepository = rankingRepository;
    }

    /**
     * Retrieves all rankings.
     *
     * @return list of rankings
     */
    public List<Ranking> findAll() {
        return rankingRepository.findAll();
    }

    /**
     * Finds a ranking by its ID.
     *
     * @param id ranking ID
     * @return the ranking if found
     * @throws IllegalArgumentException if not found
     */
    public Ranking findById(Long id) {
        return rankingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ranking not found with id: " + id));
    }

    /**
     * Saves or updates a ranking.
     *
     * @param ranking ranking entity
     * @return saved ranking
     */
    public Ranking save(Ranking ranking) {
        return rankingRepository.save(ranking);
    }

    /**
     * Deletes a ranking by its ID.
     *
     * @param id ranking ID
     */
    public void deleteById(Long id) {
        Ranking ranking = findById(id);

        if (!rankingRepository.existsById(id)) {
            throw new IllegalArgumentException("Ranking not found with id: " + id);
        }

        // Prevent deletion if referees exist
        if (!ranking.getReferees().isEmpty()) {
            throw new IllegalArgumentException("Cannot delete ranking associated with referees");
        }

        rankingRepository.deleteById(id);
    }
}