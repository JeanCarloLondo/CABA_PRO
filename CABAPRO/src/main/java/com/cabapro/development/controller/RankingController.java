/**
 * REST controller for managing Ranking entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Controller - Ranking (Admin only)
 */
package com.cabapro.development.controller;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.service.RankingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping("/admin/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("rankings", rankingService.findAll());
        return "admin/rankings/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ranking", new Ranking());
        return "admin/rankings/form";
    }

    @PostMapping
    public String save(@ModelAttribute Ranking ranking) {
        rankingService.save(ranking);
        return "redirect:/admin/rankings";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("ranking", rankingService.findById(id));
        return "admin/rankings/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id, @ModelAttribute Ranking ranking) {
        ranking.setId(id);
        rankingService.save(ranking);
        return "redirect:/admin/rankings";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Model model) {
        try {
            rankingService.deleteById(id);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("rankings", rankingService.findAll());
            return "admin/rankings/list";
        }
        return "redirect:/admin/rankings";
    }
}