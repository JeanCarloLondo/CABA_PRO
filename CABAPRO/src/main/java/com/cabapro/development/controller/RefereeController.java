package com.cabapro.development.controller;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.model.Ranking;
import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Specialty;
import com.cabapro.development.service.RefereeService;
import com.cabapro.development.service.RankingService;
import com.cabapro.development.service.SpecialtyService;
import com.cabapro.development.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/referees")
public class RefereeController {

    private final RefereeService refereeService;
    private final RankingService rankingService;
    private final SpecialtyService specialtyService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RefereeController(RefereeService refereeService,
                             RankingService rankingService,
                             SpecialtyService specialtyService,
                             PasswordEncoder passwordEncoder,
                             UserRepository userRepository) {
        this.refereeService = refereeService;
        this.rankingService = rankingService;
        this.specialtyService = specialtyService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String list(@RequestParam(value = "q", required = false) String q, Model model) {
        if (q != null && !q.isBlank()) {
            model.addAttribute("referees", refereeService.searchByEmail(q.trim()));
        } else {
            model.addAttribute("referees", refereeService.findAll());
        }
        return "admin/referees/list";
    }

    @GetMapping("/new")
    public String createForm(@RequestParam(value = "rankingId", required = false) Long rankingId,
                             Model model) {
        Referee ref = new Referee();
        if (rankingId != null) {
            ref.setRanking(rankingService.findById(rankingId)); // preselección desde Rankings
        }
        model.addAttribute("referee", ref);
        model.addAttribute("rankings", rankingService.findAll());
        model.addAttribute("specialties", specialtyService.findAll()); // <-- SIEMPRE cargo
        model.addAttribute("isCreate", true); // muestra password solo al crear
        return "admin/referees/form";
    }

    @PostMapping
    @Transactional
    public String create(@ModelAttribute Referee referee,
                         @RequestParam("rankingId") Long rankingId,
                         @RequestParam(value = "specialtyId", required = false) Long specialtyId,
                         @RequestParam("password") String password,
                         RedirectAttributes ra,
                         Model model) {
        try {
            referee.setId(null);

            Ranking ranking = rankingService.findById(rankingId);
            referee.setRanking(ranking);

            if (specialtyId != null) {
                Specialty specialty = specialtyService.findById(specialtyId);
                referee.setSpecialty(specialty);
            } else {
                referee.setSpecialty(null); // specialty opcional
            }

            // Guarda referee
            refereeService.save(referee);

            // Crea usuario de login
            CustomUser user = new CustomUser();
            String normalizedEmail = referee.getEmail().trim().toLowerCase();
            user.setName("Referee " + normalizedEmail); // evita @NotBlank en name
            user.setEmail(normalizedEmail);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("REFEREE");
            userRepository.save(user);

            ra.addFlashAttribute("success", "Referee created: " + normalizedEmail);
            return "redirect:/admin/referees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            model.addAttribute("specialties", specialtyService.findAll());
            model.addAttribute("isCreate", true);
            return "admin/referees/form";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("referee", refereeService.findById(id));
        model.addAttribute("rankings", rankingService.findAll());
        model.addAttribute("specialties", specialtyService.findAll());
        model.addAttribute("isCreate", false);
        return "admin/referees/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Referee referee,
                         @RequestParam("rankingId") Long rankingId,
                         @RequestParam(value = "specialtyId", required = false) Long specialtyId,
                         RedirectAttributes ra,
                         Model model) {
        try {
            referee.setRanking(rankingService.findById(rankingId));
            if (specialtyId != null) {
                referee.setSpecialty(specialtyService.findById(specialtyId));
            } else {
                referee.setSpecialty(null);
            }
            refereeService.update(id, referee);
            ra.addFlashAttribute("success", "Referee updated.");
            return "redirect:/admin/referees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            model.addAttribute("specialties", specialtyService.findAll());
            model.addAttribute("isCreate", false);
            return "admin/referees/form";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            refereeService.deleteById(id);
            ra.addFlashAttribute("success", "Referee deleted.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/referees";
    }
}