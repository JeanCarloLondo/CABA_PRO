package com.cabapro.development.controller;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Specialty;
import com.cabapro.development.repository.RankingRepository;
import com.cabapro.development.repository.RefereeRepository;
import com.cabapro.development.repository.SpecialtyRepository;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Administrator dashboard: shows referees, rankings and specialties.
 */
@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

  private final RefereeRepository refereeRepo;
  private final RankingRepository rankingRepo;
  private final SpecialtyRepository specialtyRepo;

  public AdminDashboardController(RefereeRepository refereeRepo,
                                  RankingRepository rankingRepo,
                                  SpecialtyRepository specialtyRepo) {
    this.refereeRepo = refereeRepo;
    this.rankingRepo = rankingRepo;
    this.specialtyRepo = specialtyRepo;
  }

  @GetMapping("/dashboard")
  public String dashboard(Model model,
                          @ModelAttribute("success") String success,
                          @ModelAttribute("error") String error) {
    List<Referee> referees = refereeRepo.findAll();
    List<Ranking> rankings = rankingRepo.findAll();
    List<Specialty> specialties = specialtyRepo.findAll();

    model.addAttribute("referees", referees);
    model.addAttribute("rankings", rankings);
    model.addAttribute("specialties", specialties);

    if (success != null && !success.isBlank()) model.addAttribute("success", success);
    if (error != null && !error.isBlank()) model.addAttribute("error", error);

    return "admin/dashboard";
  }
}
