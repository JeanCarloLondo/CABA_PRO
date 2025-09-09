package com.cabapro.development.repository;

import com.cabapro.development.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {

    // Para métricas "scheduled vs completed"
    long countByMatchDateAfter(LocalDateTime now);   // scheduled (futuros)
    long countByMatchDateBefore(LocalDateTime now);  // completed (pasados)

    // Útil para rangos (por ejemplo, mes actual)
    long countByMatchDateBetween(LocalDateTime start, LocalDateTime end);
}