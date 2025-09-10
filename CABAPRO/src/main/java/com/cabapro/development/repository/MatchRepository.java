package com.cabapro.development.repository;

import com.cabapro.development.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    long countByMatchDateAfter(LocalDateTime now);   
    long countByMatchDateBefore(LocalDateTime now);  

    long countByMatchDateBetween(LocalDateTime start, LocalDateTime end);
}