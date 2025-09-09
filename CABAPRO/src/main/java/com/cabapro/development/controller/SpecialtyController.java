package com.cabapro.development.controller;

import com.cabapro.development.model.Referee;
import com.cabapro.development.model.Specialty;
import com.cabapro.development.service.RefereeService;
import com.cabapro.development.service.SpecialtyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/specialty")
public class SpecialtyController {

    private final SpecialtyService specialtyService;
    private final RefereeService refereeService;

    public SpecialtyController(SpecialtyService specialtyService,
                               RefereeService refereeService) {
        this.specialtyService = specialtyService;
        this.refereeService = refereeService;
    }

    // LIST
    @GetMapping
    public String list(Model model,
                       @ModelAttribute("success") String success,
                       @ModelAttribute("error") String error) {
        model.addAttribute("specialty", specialtyService.findAll());
        if (success != null && !success.isBlank()) model.addAttribute("success", success);
        if (error != null && !error.isBlank()) model.addAttribute("error", error);
        return "admin/specialty/list";
    }

    // CREATE FORM
    @GetMapping("/new")
    public String createForm(Model model) {
        if (!model.containsAttribute("specialty")) model.addAttribute("specialty", new Specialty());
        return "admin/specialty/form";
    }

    // CREATE
    @PostMapping
    public String create(@ModelAttribute Specialty specialty, RedirectAttributes ra) {
        try {
            specialtyService.create(specialty);
            ra.addFlashAttribute("success", "Specialty created successfully.");
            return "redirect:/admin/specialty";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("specialty", specialty);
            return "redirect:/admin/specialty/new";
        }
    }

    // EDIT FORM
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            if (!model.containsAttribute("specialty"))
                model.addAttribute("specialty", specialtyService.findById(id));
            return "admin/specialty/form";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/specialty";
        }
    }

    // UPDATE
    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute Specialty specialty,
                         RedirectAttributes ra) {
        try {
            specialtyService.update(id, specialty);
            ra.addFlashAttribute("success", "Specialty updated successfully.");
            return "redirect:/admin/specialty";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("specialty", specialty);
            return "redirect:/admin/specialty/" + id + "/edit";
        }
    }

    // DELETE (con regla: no borrar si está en uso)
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            specialtyService.deleteById(id);
            ra.addFlashAttribute("success", "Specialty deleted.");
        } catch (IllegalStateException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/specialty";
    }

    // ====== Referees asociados a una specialty ======

    @GetMapping("/{id}/referees")
    public String referees(@PathVariable Long id, Model model, RedirectAttributes ra) {
        try {
            Specialty sp = specialtyService.findById(id);
            model.addAttribute("specialty", sp);
            model.addAttribute("referees", refereeService.findBySpecialty(id));
            // Para asignar rápido: lista de árbitros sin specialty
            model.addAttribute("availableReferees", refereeService.findWithoutSpecialty());
            return "admin/specialty/referees";
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/specialty";
        }
    }

    // Asignar un referee a esta specialty
    @PostMapping("/{id}/assign")
    public String assign(@PathVariable Long id,
                         @RequestParam("refereeId") Long refereeId,
                         RedirectAttributes ra) {
        try {
            Specialty sp = specialtyService.findById(id);
            Referee ref = refereeService.findById(refereeId);
            refereeService.assignSpecialty(ref, sp);
            ra.addFlashAttribute("success", "Referee assigned to specialty.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/specialty/" + id + "/referees";
    }

    // Quitar specialty del referee
    @PostMapping("/{id}/unassign")
    public String unassign(@PathVariable Long id,
                           @RequestParam("refereeId") Long refereeId,
                           RedirectAttributes ra) {
        try {
            Referee ref = refereeService.findById(refereeId);
            refereeService.unassignSpecialty(ref);
            ra.addFlashAttribute("success", "Specialty removed from referee.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/specialty/" + id + "/referees";
    }
}