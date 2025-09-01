/**
 * Repository interface for managing Ranking entities.
 *
 * Author: Team #5
 * Date: 2025-08-31
 * Role: Data access layer - Ranking
 */
package com.cabapro.development.repository;

import com.cabapro.development.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    /**
     * Finds a ranking by its name.
     *
     * @param name the ranking name (e.g., FIBA, National, Local)
     * @return an Optional containing the Ranking if found, otherwise empty
     */
    Optional<Ranking> findByName(String name);
}