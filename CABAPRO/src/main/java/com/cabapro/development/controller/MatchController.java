/**
 *
 * Author: Alejandra Ortiz
 * Date: 2025-09-03
 * Role: Controller - Match 
 */
package com.cabapro.development.controller;

import com.cabapro.development.model.Match;
import com.cabapro.development.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/matches")
public class MatchController {
    @Autowired
    private MatchRepository matchRepo;

    // Match list
    @GetMapping
    public String listMatches(Model model) {
        List<Match> matches = matchRepo.findAll();
        model.addAttribute("matches", matches);
        return "match/list"; // resources/templates/match/list.html
    }

    // Match details
    @GetMapping("/{id}")
    public String detailMatch(@PathVariable Long id, Model model) {
        Optional<Match> match = matchRepo.findById(id);
        if (match.isPresent()) {
            model.addAttribute("match", match.get());
            return "match/detail"; // resources/templates/match/detail.html
        } else {
            return "redirect:/matches";
        }
    }

    // Forms to create new match
    @GetMapping("/create")
    public String createMatchForm(Model model) {
        model.addAttribute("match", new Match());
        return "match/form";
    }

    // Save new match
    @PostMapping("/create")
    public String saveMatch(@ModelAttribute Match match) {
        matchRepo.save(match);
        return "redirect:/matches";
    }

    // Forms to edit match
    @GetMapping("/edit/{id}")
    public String editMatchForm(@PathVariable Long id, Model model) {
        Optional<Match> match = matchRepo.findById(id);
        if (match.isPresent()) {
            model.addAttribute("match", match.get());
            return "match/form";
        } else {
            return "redirect:/matches";
        }
    }

    // Save edited match
    @PostMapping("/edit/{id}")
    public String updateMatch(@PathVariable Long id, @ModelAttribute Match match) {
        match.setId(id);
        matchRepo.save(match);
        return "redirect:/matches";
    }

    //  Delete match
    @GetMapping("/delete/{id}")
    public String deleteMatch(@PathVariable Long id) {
        matchRepo.deleteById(id);
        return "redirect:/matches";
    }
}
