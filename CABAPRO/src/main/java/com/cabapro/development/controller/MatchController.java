package com.cabapro.development.controller;

import com.cabapro.development.model.Match;
import com.cabapro.development.service.MatchService;
import com.cabapro.development.repository.TournamentRepository;
import com.cabapro.development.repository.AssignmentRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin/matches")
public class MatchController {

    private final MatchService matchService;
    private final TournamentRepository tournamentRepo;
    private final AssignmentRepository assignmentRepo;

    public MatchController(MatchService matchService,
                           TournamentRepository tournamentRepo,
                           AssignmentRepository assignmentRepo) {
        this.matchService = matchService;
        this.tournamentRepo = tournamentRepo;
        this.assignmentRepo = assignmentRepo;
    }

    // LIST
    @GetMapping
    public String list(Model model,
                       @ModelAttribute("success") String success,
                       @ModelAttribute("error") String error) {
        model.addAttribute("matches", matchService.findAll());
        if (success != null && !success.isBlank()) model.addAttribute("success", success);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "admin/matches/list";
    }

    // NEW FORM
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("match", new Match());
        model.addAttribute("tournaments", tournamentRepo.findAll());
        return "admin/matches/form";
    }

    // CREATE
    @PostMapping
    public String create(@RequestParam Long tournamentId,
                         @RequestParam String location,
                         @RequestParam("matchDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime matchDate,
                         @RequestParam(required = false) String homeTeam,
                         @RequestParam(required = false) String awayTeam,
                         RedirectAttributes ra) {
        try {
            matchService.create(tournamentId, location, matchDate, homeTeam, awayTeam);
            ra.addFlashAttribute("success", "Match created successfully.");
            return "redirect:/admin/matches";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/matches/new";
        }
    }

    // EDIT FORM
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("match", matchService.findById(id));
        model.addAttribute("tournaments", tournamentRepo.findAll());
        return "admin/matches/form";
    }

    // UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam Long tournamentId,
                         @RequestParam String location,
                         @RequestParam("matchDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime matchDate,
                         @RequestParam(required = false) String homeTeam,
                         @RequestParam(required = false) String awayTeam,
                         RedirectAttributes ra) {
        try {
            matchService.update(id, tournamentId, location, matchDate, homeTeam, awayTeam);
            ra.addFlashAttribute("success", "Match updated successfully.");
            return "redirect:/admin/matches";
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
            return "redirect:/admin/matches/" + id + "/edit";
        }
    }

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        matchService.delete(id);
        ra.addFlashAttribute("success", "Match deleted.");
        return "redirect:/admin/matches";
    }

    // ASSIGNMENTS UI
    @GetMapping("/{id}/assign")
    public String assignView(@PathVariable Long id, Model model,
                             @ModelAttribute("success") String success,
                             @ModelAttribute("error") String error) {
        model.addAttribute("match", matchService.findById(id));
        model.addAttribute("assignments", matchService.getAssignments(id));
        model.addAttribute("eligibleReferees", matchService.eligibleReferees());
        if (success != null && !success.isBlank()) model.addAttribute("success", success);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "admin/matches/assign";
    }

    @PostMapping("/{id}/assign")
    public String assignReferee(@PathVariable Long id,
                                @RequestParam Long refereeId,
                                RedirectAttributes ra) {
        try {
            matchService.assignReferee(id, refereeId);
            ra.addFlashAttribute("success", "Referee assigned.");
        } catch (IllegalArgumentException ex) {
            ra.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/admin/matches/" + id + "/assign";
    }

    @PostMapping("/{id}/assign/{assignmentId}/remove")
    public String unassign(@PathVariable Long id,
                           @PathVariable Long assignmentId,
                           RedirectAttributes ra) {
        matchService.unassign(assignmentId);
        ra.addFlashAttribute("success", "Assignment removed.");
        return "redirect:/admin/matches/" + id + "/assign";
    }
}