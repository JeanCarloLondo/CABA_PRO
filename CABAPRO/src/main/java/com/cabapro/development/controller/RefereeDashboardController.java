package com.cabapro.development.controller;

/**
 * Controller for Referee Dashboard.
 *
 * Author: Jean Londoño
 * Date: 2025-09-03
 * Role: Referee profile view
 */

import com.cabapro.development.model.Referee;
import com.cabapro.development.service.RefereeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class RefereeDashboardController {

    private final RefereeService refereeService;

    public RefereeDashboardController(RefereeService refereeService) {
        this.refereeService = refereeService;
    }

    /**
     * Shows the dashboard with referee profile.
     *
     * @param id referee ID (simulated until login integration)
     * @param model thymeleaf model
     * @return referee dashboard view
     */
    @GetMapping("/referee/dashboard/{id}")
    public String viewDashboard(@PathVariable Long id, Model model) {
        Referee referee = refereeService.findById(id);
        model.addAttribute("referee", referee);
        return "referee/dashboard";
    }
}