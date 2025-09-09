package com.cabapro.development.controller;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.model.Referee;
import com.cabapro.development.service.RankingService;
import com.cabapro.development.service.RefereeService;
import com.cabapro.development.service.SpecialtyService;
import com.cabapro.development.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing Referee entities (Admin only).
 */
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

    /** List all referees */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("referees", refereeService.findAll());
        return "admin/referees/list";
    }

    /** Search referees (by email only) */
    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        model.addAttribute("referees", refereeService.searchByEmail(query));
        return "admin/referees/list";
    }

    /** Show form for creating referee */
    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("referee", new Referee());
        model.addAttribute("rankings", rankingService.findAll());
        model.addAttribute("specialties", specialtyService.findAll());
        return "admin/referees/form";
    }

    /** Create referee */
    @PostMapping
    public String create(@ModelAttribute Referee referee,
                         @RequestParam("name") String name,
                         @RequestParam("rankingId") Long rankingId,
                         @RequestParam("specialtyId") Long specialtyId,
                         @RequestParam("password") String password,
                         Model model) {
        try {
            referee.setId(null);
            referee.setRanking(rankingService.findById(rankingId));
            referee.setSpecialty(specialtyService.findById(specialtyId));
            refereeService.save(referee);

            // Create associated CustomUser for login
            CustomUser user = new CustomUser();
            user.setName(name); // requerido por @NotBlank en CustomUser
            user.setEmail(referee.getEmail());
            user.setPassword(passwordEncoder.encode(password));
            user.setRole("REFEREE");
            userRepository.save(user);

            return "redirect:/admin/referees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            model.addAttribute("specialties", specialtyService.findAll());
            return "admin/referees/form";
        }
    }

    /** Show form for editing referee */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("referee", refereeService.findById(id));
        model.addAttribute("rankings", rankingService.findAll());
        model.addAttribute("specialties", specialtyService.findAll());
        return "admin/referees/form";
    }

    /** Update referee */
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Referee referee,
                         @RequestParam("name") String name,
                         @RequestParam("rankingId") Long rankingId,
                         @RequestParam("specialtyId") Long specialtyId,
                         Model model) {
        try {
            referee.setRanking(rankingService.findById(rankingId));
            referee.setSpecialty(specialtyService.findById(specialtyId));
            refereeService.update(id, referee);

            // Update linked CustomUser's name (email is the key)
            userRepository.findByEmail(referee.getEmail()).ifPresent(u -> {
                u.setName(name);
                userRepository.save(u);
            });

            return "redirect:/admin/referees";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            model.addAttribute("specialties", specialtyService.findAll());
            return "admin/referees/form";
        }
    }

    /** Delete referee */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        refereeService.deleteById(id);
        return "redirect:/admin/referees";
    }
}