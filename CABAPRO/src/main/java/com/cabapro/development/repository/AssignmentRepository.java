package com.cabapro.development.repository;

import com.cabapro.development.model.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // --- consultas por partido ---
    List<Assignment> findByMatch_IdMatch(Long matchId);
    void deleteByMatch_IdMatch(Long matchId);

    // --- consultas por árbitro ---
    // Spring Data permite "Referee_Id" y también "RefereeId" (traversal implícito)
    List<Assignment> findByReferee_Id(Long refereeId);
    List<Assignment> findByRefereeId(Long refereeId); // <-- compat con tu servicio

    // --- existencia de asignación partido-árbitro (usa PK de Match = idMatch) ---
    boolean existsByMatch_IdMatchAndReferee_Id(Long matchId, Long refereeId);

    // (Si algún código tuyo usa otra forma, puedes añadir también este alias)
    // boolean existsByMatchIdAndRefereeId(Long matchId, Long refereeId);

    // --- top árbitros en rango (p.ej. mes actual) ---
    @Query("""
           select a.referee.id as refereeId, count(a) as matchesCount
           from Assignment a
           where a.match.matchDate between :start and :end
           group by a.referee.id
           order by matchesCount desc
           """)
    List<Object[]> topRefereesBetween(@Param("start") LocalDateTime start,
                                      @Param("end")   LocalDateTime end,
                                      Pageable pageable);
}