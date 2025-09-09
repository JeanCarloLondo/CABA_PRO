package com.cabapro.development.repository;

import com.cabapro.development.model.Referee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefereeRepository extends JpaRepository<Referee, Long> {

    // Búsquedas básicas
    Optional<Referee> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Referee> findByEmailContainingIgnoreCase(String email);

    // Ranking
    long countByRankingId(Long rankingId);
    List<Referee> findByRankingId(Long rankingId);

    // Specialty (la PK en Specialty es idSpecialty)
    List<Referee> findBySpecialtyIsNull();
    List<Referee> findBySpecialty_IdSpecialty(Long idSpecialty);
    long countBySpecialty_IdSpecialty(Long idSpecialty);
}
