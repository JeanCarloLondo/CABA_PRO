/**
 * Service layer for managing Assignment entities.
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Business logic - Assignment
 */

package com.cabapro.development.service;

import com.cabapro.development.model.Assignment;
import com.cabapro.development.model.AssignmentStatus;
import com.cabapro.development.model.Referee;
import com.cabapro.development.repository.AssignmentRepository;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final RefereeRepository refereeRepository;
    private final EmailService emailService;

    public AssignmentService(AssignmentRepository assignmentRepository, RefereeRepository refereeRepository, EmailService emailService) {
        this.assignmentRepository = assignmentRepository;
        this.refereeRepository = refereeRepository;
        this.emailService = emailService;
    }

    public List<Assignment> findAll() {
        return assignmentRepository.findAll();
    }

    public Assignment reassign(Long assignmentId, Long newRefereeId) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with id: " + assignmentId));

        Referee newReferee = assignment.getAdmin() == null
                ? null
                : assignment.getAdmin().getAssignments().get(0).getReferee(); // dummy check, real logic later

        newReferee = refereeRepository.findById(newRefereeId)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found with id: " + newRefereeId));

        assignment.setReferee(newReferee);
        assignment.setStatus(AssignmentStatus.PENDING);
        return assignmentRepository.save(assignment);
    }

    public List<Assignment> findByRefereeId(Long refereeId) {
        return assignmentRepository.findByRefereeId(refereeId);
    }

    public Assignment updateStatus(Long id, AssignmentStatus status) {
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with id: " + id));
        assignment.setStatus(status);
        return assignmentRepository.save(assignment);
    }
}