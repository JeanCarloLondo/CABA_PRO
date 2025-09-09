package com.cabapro.development.repository;

import com.cabapro.development.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    List<Assignment> findByMatch_IdMatch(Long matchId);

    boolean existsByMatch_IdMatchAndReferee_Id(Long matchId, Long refereeId);

    long countByMatch_IdMatch(Long matchId);

    // Para tu AssignmentService:
    List<Assignment> findByRefereeId(Long refereeId);

    // Variante equivalente (útil si en otros lados la llamaste así):
    List<Assignment> findByReferee_Id(Long refereeId);
}