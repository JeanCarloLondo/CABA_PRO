package com.cabapro.development.repository;

import com.cabapro.development.model.Assignment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // --- Por partido ---
    List<Assignment> findByMatch_IdMatch(Long matchId);
    void deleteByMatch_IdMatch(Long matchId);

    // --- Por árbitro ---
    List<Assignment> findByReferee_Id(Long refereeId);
    // alias (si tu código en algún punto usa esta variante)
    List<Assignment> findByRefereeId(Long refereeId);

    // --- Existencia partido-árbitro ---
    boolean existsByMatch_IdMatchAndReferee_Id(Long matchId, Long refereeId);

    // --- Rango de fechas (útil para estadísticas generales) ---
    List<Assignment> findByMatch_MatchDateBetween(LocalDateTime start, LocalDateTime end);

    // --- Top árbitros entre fechas (para /admin/statistics) ---
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

    // --- Liquidaciones: traer asignaciones con joins (match, tournament, referee, ranking) ordenadas por fecha ---
    @EntityGraph(attributePaths = {"match", "match.tournament", "referee", "ranking"})
    List<Assignment> findByReferee_IdAndMatch_MatchDateBetweenOrderByMatch_MatchDateAsc(
            Long refereeId, LocalDateTime start, LocalDateTime end);

    // --- Liquidaciones: referees con al menos una asignación en el rango ---
    @Query("""
           select distinct a.referee.id
           from Assignment a
           where a.match.matchDate between :start and :end
           """)
    List<Long> findDistinctRefereeIdsBetween(@Param("start") LocalDateTime start,
                                             @Param("end")   LocalDateTime end);

    // --- Variante por mes usada en algunos dashboards ---
    @Query("""
           select a
           from Assignment a
           where a.referee.id = :refId
             and a.match.matchDate between :start and :end
           order by a.match.matchDate asc
           """)
    List<Assignment> findForRefereeInMonth(@Param("refId") Long refereeId,
                                           @Param("start") LocalDateTime start,
                                           @Param("end") LocalDateTime end);
}