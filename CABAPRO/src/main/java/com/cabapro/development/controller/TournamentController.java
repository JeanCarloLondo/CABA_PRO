// src/main/java/com/cabapro/development/controller/TournamentController.java
package com.cabapro.development.controller;

import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Tournament;
import com.cabapro.development.service.TournamentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/tournaments")
public class TournamentController {

    private final TournamentService tournamentService;

    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    // LIST
    @GetMapping
    public String list(@ModelAttribute("success") String success,
                       @ModelAttribute("error") String error,
                       Model model) {
        model.addAttribute("tournaments", tournamentService.findAll());
        if (success != null && !success.isBlank()) model.addAttribute("success", success);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "admin/tournaments/list";
    }

    // NEW
    @GetMapping("/new")
    public String formNew(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "admin/tournaments/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String teams,
                         @RequestParam(required = false) String rounds,
                         RedirectAttributes ra) {
        try {
            tournamentService.create(name, teams, rounds);
            ra.addFlashAttribute("success", "Tournament created.");
            return "redirect:/admin/tournaments";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/tournaments/new";
        }
    }

    // EDIT
    @GetMapping("/{id}/edit")
    public String formEdit(@PathVariable Long id, Model model) {
        model.addAttribute("tournament", tournamentService.findById(id));
        return "admin/tournaments/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String teams,
                         @RequestParam(required = false) String rounds,
                         RedirectAttributes ra) {
        try {
            tournamentService.update(id, name, teams, rounds);
            ra.addFlashAttribute("success", "Tournament updated.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/tournaments";
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            tournamentService.delete(id);
            ra.addFlashAttribute("success", "Tournament deleted.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/tournaments";
    }

    // REFEREES (assign/remove)
    @GetMapping("/{id}/referees")
    public String referees(@PathVariable Long id, Model model) {
        Tournament t = tournamentService.findById(id);
        Set<Referee> assigned = tournamentService.assignedReferees(id);
        List<Referee> candidates = tournamentService.unassignedReferees(id);

        model.addAttribute("tournament", t);
        model.addAttribute("assigned", assigned);
        model.addAttribute("candidates", candidates);
        return "admin/tournaments/referees";
    }

    @PostMapping("/{id}/referees/add")
    public String addReferee(@PathVariable Long id,
                             @RequestParam Long refereeId,
                             RedirectAttributes ra) {
        try {
            tournamentService.addReferee(id, refereeId);
            ra.addFlashAttribute("success", "Referee assigned.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/tournaments/{id}/referees";
    }

    @PostMapping("/{id}/referees/{refId}/remove")
    public String removeReferee(@PathVariable Long id,
                                @PathVariable("refId") Long refereeId,
                                RedirectAttributes ra) {
        tournamentService.removeReferee(id, refereeId);
        ra.addFlashAttribute("success", "Referee removed.");
        return "redirect:/admin/tournaments/{id}/referees";
    }
}