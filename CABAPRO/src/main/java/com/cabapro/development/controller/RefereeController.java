/**
 * REST controller for managing Referee entities.
 *
 * Author: Jean Londoño
 * Date: 2025-08-31
 * Role: Controller - Referee (Admin only)
 */

package com.cabapro.development.controller;

import com.cabapro.development.model.Ranking;
import com.cabapro.development.model.Referee;
import com.cabapro.development.service.RefereeService;
import com.cabapro.development.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/admin/referees")
public class RefereeController {

    private final RefereeService refereeService;
    private final RankingService rankingService;

    public RefereeController(RefereeService refereeService, RankingService rankingService) {
        this.refereeService = refereeService;
        this.rankingService = rankingService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("referees", refereeService.findAll());
        return "admin/referees/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("referee", new Referee());
        model.addAttribute("rankings", rankingService.findAll());
        return "admin/referees/form";
    }

    @PostMapping
    public String create(@ModelAttribute Referee referee,
            @RequestParam("rankingId") Long rankingId,
            Model model) {
        try {

            referee.setId(null);
            Ranking ranking = rankingService.findById(rankingId);
            referee.setRanking(ranking);
            refereeService.save(referee);
            return "redirect:/admin/referees";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            return "admin/referees/form";
            
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("referee", refereeService.findById(id));
        model.addAttribute("rankings", rankingService.findAll());
        return "admin/referees/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
            @ModelAttribute Referee referee,
            @RequestParam("rankingId") Long rankingId,
            Model model) {
        try {

            Ranking ranking = rankingService.findById(rankingId);
            referee.setRanking(ranking);
            refereeService.update(id, referee);
            return "redirect:/admin/referees";

        } catch (IllegalArgumentException e) {

            model.addAttribute("error", e.getMessage());
            model.addAttribute("referee", referee);
            model.addAttribute("rankings", rankingService.findAll());
            return "admin/referees/form";
            
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        refereeService.deleteById(id);
        return "redirect:/admin/referees";
    }
}