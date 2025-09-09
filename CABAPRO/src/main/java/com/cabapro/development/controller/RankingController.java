package com.cabapro.development.controller;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.service.RankingService;
import com.cabapro.development.service.RefereeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/rankings")
public class RankingController {

    private final RankingService rankingService;
    private final RefereeService refereeService; // ⬅️ nuevo

    public RankingController(RankingService rankingService,
                             RefereeService refereeService) {
        this.rankingService = rankingService;
        this.refereeService = refereeService;
    }

    @GetMapping
    public String list(Model model,
                       @ModelAttribute("success") String success,
                       @ModelAttribute("error") String error) {
        model.addAttribute("rankings", rankingService.findAll());
        if (success != null && !success.isBlank()) model.addAttribute("success", success);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "admin/rankings/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("ranking")) {
            model.addAttribute("ranking", new Ranking());
        }
        return "admin/rankings/form";
    }

    @PostMapping
    public String create(@ModelAttribute Ranking ranking, RedirectAttributes ra) {
        try {
            rankingService.create(ranking);
            ra.addFlashAttribute("success", "Ranking created successfully.");
            return "redirect:/admin/rankings";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("ranking", ranking);
            return "redirect:/admin/rankings/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            if (!model.containsAttribute("ranking")) {
                model.addAttribute("ranking", rankingService.findById(id));
            }
            return "admin/rankings/form";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/rankings";
        }
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Ranking ranking,
                         RedirectAttributes ra) {
        try {
            rankingService.update(id, ranking);
            ra.addFlashAttribute("success", "Ranking updated successfully.");
            return "redirect:/admin/rankings";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("ranking", ranking);
            return "redirect:/admin/rankings/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            rankingService.deleteById(id);
            ra.addFlashAttribute("success", "Ranking deleted.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/rankings";
    }

    
    @GetMapping("/{id}/referees")
    public String refereesByRanking(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            var ranking = rankingService.findById(id);
            model.addAttribute("ranking", ranking);
            model.addAttribute("referees", refereeService.findByRanking(id));
            return "admin/rankings/referees";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/rankings";
        }
    }
}