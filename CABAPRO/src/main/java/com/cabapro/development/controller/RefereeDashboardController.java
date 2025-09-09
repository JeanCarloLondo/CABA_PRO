package com.cabapro.development.controller;

import com.cabapro.development.model.Referee;
import com.cabapro.development.repository.AssignmentRepository;
import com.cabapro.development.repository.RefereeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RefereeDashboardController {

    private final RefereeRepository refereeRepo;
    private final AssignmentRepository assignmentRepo;

    public RefereeDashboardController(RefereeRepository refereeRepo,
                                      AssignmentRepository assignmentRepo) {
        this.refereeRepo = refereeRepo;
        this.assignmentRepo = assignmentRepo;
    }

    @GetMapping("/referee/matches")
    public String myMatches(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Referee me = refereeRepo.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found for " + email));
        model.addAttribute("assignments", assignmentRepo.findByReferee_Id(me.getId()));
        return "referee/matches";
    }
}
