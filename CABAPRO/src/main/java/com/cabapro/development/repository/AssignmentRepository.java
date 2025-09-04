/**
 * Repository interface for managing Assignment entities.
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Data access layer - Assignment
 */

package com.cabapro.development.repository;

import com.cabapro.development.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByRefereeId(Long refereeId);
}