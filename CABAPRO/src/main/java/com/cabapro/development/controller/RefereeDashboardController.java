package com.cabapro.development.controller;

/**
 * Controller for Referee Dashboard.
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Referee profile view
 */

import com.cabapro.development.model.AssignmentStatus;
import com.cabapro.development.service.AssignmentService;
import com.cabapro.development.service.RefereeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/referee/dashboard")
public class RefereeDashboardController {

    private final RefereeService refereeService;
    private final AssignmentService assignmentService;

    public RefereeDashboardController(RefereeService refereeService,
            AssignmentService assignmentService) {
        this.refereeService = refereeService;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/{id}")
    public String viewDashboard(@PathVariable Long id, Model model) {
        var referee = refereeService.findById(id);
        model.addAttribute("referee", referee);
        model.addAttribute("assignments", assignmentService.findByRefereeId(id));
        return "referee/dashboard";
    }

    @PostMapping("/{refereeId}/assignments/{assignmentId}/accept")
    public String acceptAssignment(@PathVariable Long refereeId,
            @PathVariable Long assignmentId) {
        assignmentService.updateStatus(assignmentId, AssignmentStatus.ACCEPTED);
        return "redirect:/referee/dashboard/" + refereeId;
    }

    @PostMapping("/{refereeId}/assignments/{assignmentId}/reject")
    public String rejectAssignment(@PathVariable Long refereeId,
            @PathVariable Long assignmentId) {
        assignmentService.updateStatus(assignmentId, AssignmentStatus.REJECTED);
        return "redirect:/referee/dashboard/" + refereeId;
    }
}