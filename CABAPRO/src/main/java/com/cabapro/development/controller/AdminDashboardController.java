package com.cabapro.development.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import com.cabapro.development.service.RefereeService;
import com.cabapro.development.service.SpecialtyService;
import com.cabapro.development.service.RankingService;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final RefereeService refereeService;
    private final RankingService rankingService;
    private final SpecialtyService specialtyService;

    public AdminDashboardController(RefereeService refereeService, RankingService rankingService, SpecialtyService specialtyService) {
        this.refereeService = refereeService;
        this.rankingService = rankingService;
        this.specialtyService = specialtyService;
    }

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("referees", refereeService.findAll());
        model.addAttribute("rankings", rankingService.findAll());
        model.addAttribute("specialties", specialtyService.findAll());
        return "admin/dashboard";
    }
}
