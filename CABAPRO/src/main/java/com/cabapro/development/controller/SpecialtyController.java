package com.cabapro.development.controller;

import com.cabapro.development.model.Specialty;
import com.cabapro.development.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/specialties")
public class SpecialtyController {
    @Autowired
    private SpecialtyRepository specialtyRepo;

    // list of specialties
    @GetMapping
    public String listSpecialties(Model model) {
        List<Specialty> specialties = specialtyRepo.findAll();
        model.addAttribute("specialties", specialties);
        return "specialty/list"; // resources/templates/specialty/list.html
    }

    // see details of a specialty
    @GetMapping("/{id}")
    public String detailSpecialty(@PathVariable Long id, Model model) {
        Optional<Specialty> specialty = specialtyRepo.findById(id);
        if (specialty.isPresent()) {
            model.addAttribute("specialty", specialty.get());
            return "specialty/detail"; // resources/templates/specialty/detail.html
        } else {
            return "redirect:/specialties";
        }
    }

    // form to create new specialty
    @GetMapping("/create")
    public String createSpecialtyForm(Model model) {
        model.addAttribute("specialty", new Specialty());
        return "specialty/form";
    }

    // save new specialty
    @PostMapping("/create")
    public String saveSpecialty(@ModelAttribute Specialty specialty) {
        specialtyRepo.save(specialty);
        return "redirect:/specialties";
    }

    // form to edit specialty
    @GetMapping("/edit/{id}")
    public String editSpecialtyForm(@PathVariable Long id, Model model) {
        Optional<Specialty> specialty = specialtyRepo.findById(id);
        if (specialty.isPresent()) {
            model.addAttribute("specialty", specialty.get());
            return "specialty/form";
        } else {
            return "redirect:/specialties";
        }
    }

    // save edited specialty
    @PostMapping("/edit/{id}")
    public String updateSpecialty(@PathVariable Long id, @ModelAttribute Specialty specialty) {
        specialty.setIdSpecialty(id);
        specialtyRepo.save(specialty);
        return "redirect:/specialties";
    }

    // delete
    @GetMapping("/delete/{id}")
    public String deleteSpecialty(@PathVariable Long id) {
        specialtyRepo.deleteById(id);
        return "redirect:/specialties";
    }
}