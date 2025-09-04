/**
 * Controller for managing referee assignments (Admin only).
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Admin view for assignments
 */
package com.cabapro.development.controller;

import com.cabapro.development.service.AssignmentService;
import com.cabapro.development.service.RefereeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/assignments")
public class AssignmentAdminController {

    private final AssignmentService assignmentService;
    private final RefereeService refereeService;

    public AssignmentAdminController(AssignmentService assignmentService,
            RefereeService refereeService) {
        this.assignmentService = assignmentService;
        this.refereeService = refereeService;
    }

    @GetMapping
    public String list(@RequestParam(value = "refereeId", required = false) Long refereeId, Model model) {
        if (refereeId != null) {
            model.addAttribute("assignments", assignmentService.findByRefereeId(refereeId));
        } else {
            model.addAttribute("assignments", assignmentService.findAll());
        }
        model.addAttribute("referees", refereeService.findAll());
        return "admin/assignments/list";
    }

    @PostMapping("/{id}/reassign")
    public String reassign(@PathVariable Long id, @RequestParam("newRefereeId") Long newRefereeId) {
        assignmentService.reassign(id, newRefereeId);
        return "redirect:/admin/assignments";
    }
}