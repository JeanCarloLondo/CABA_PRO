package com.cabapro.development.controller;

import com.cabapro.development.model.AssignmentStatus;
import com.cabapro.development.model.Referee;
import com.cabapro.development.repository.AssignmentRepository;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.service.AssignmentService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RefereeDashboardController {

    private final RefereeRepository refereeRepo;
    private final AssignmentRepository assignmentRepo;
    private final AssignmentService assignmentService;

    public RefereeDashboardController(RefereeRepository refereeRepo,
            AssignmentRepository assignmentRepo, AssignmentService assignmentService) {
        this.refereeRepo = refereeRepo;
        this.assignmentRepo = assignmentRepo;
        this.assignmentService = assignmentService;
    }

    @GetMapping("/referee/dashboard/{id}")
    public String dashboard(@PathVariable Long id, Model model) {
        Referee referee = refereeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + id));
        model.addAttribute("referee", referee);
        model.addAttribute("assignments", assignmentRepo.findByReferee_Id(id));
        return "referee/dashboard"; // tu template actual
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

    @PostMapping("/referee/dashboard/{refId}/assignments/{aId}/accept")
    public String acceptAssignment(@PathVariable Long refId,
            @PathVariable Long aId) {
        assignmentService.updateStatus(aId, AssignmentStatus.ACCEPTED);
        return "redirect:/referee/dashboard/" + refId;
    }

    @PostMapping("/referee/dashboard/{refId}/assignments/{aId}/reject")
    public String rejectAssignment(@PathVariable Long refId,
            @PathVariable Long aId) {
        assignmentService.updateStatus(aId, AssignmentStatus.REJECTED);
        return "redirect:/referee/dashboard/" + refId;
    }

    @PostMapping("/referee/dashboard/{id}/update")
    public String updateProfile(@PathVariable Long id,
            String email,
            String phoneNumber) {
        Referee referee = refereeRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Referee not found: " + id));
        referee.setEmail(email);
        referee.setPhoneNumber(phoneNumber);
        refereeRepo.save(referee);
        return "redirect:/referee/dashboard/" + id;
    }
}