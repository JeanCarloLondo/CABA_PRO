package com.cabapro.development.repository;

import com.cabapro.development.model.Ranking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {

    /** Búsqueda exacta (case-insensitive) por nombre */
    Optional<Ranking> findByNameIgnoreCase(String name);

    /** Verificación de existencia por nombre (case-insensitive) */
    boolean existsByNameIgnoreCase(String name);
}
