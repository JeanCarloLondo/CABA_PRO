// src/main/java/com/cabapro/development/repository/TournamentRepository.java
package com.cabapro.development.repository;

import com.cabapro.development.model.Tournament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    boolean existsByNameIgnoreCase(String name);
    Optional<Tournament> findByNameIgnoreCase(String name);

    // Activos = con al menos un partido futuro
    @Query("""
           select distinct t
           from Tournament t
             join t.matches m
           where m.matchDate >= :since
           order by t.name asc
           """)
    List<Tournament> findActiveSince(LocalDateTime since);
}