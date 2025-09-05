/**
 *  Controller for managing user-related actions.
 *
 * Author: Jean Londoño and Alejandra Ortiz
 * Date: 2025-09-05
 * Role: User Management
 */
package com.cabapro.development.controller;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- LIST USERS ---
    @GetMapping
    public String listUsers(Model model) {
        List<CustomUser> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "user/list"; // resources/templates/user/list.html
    }

    // --- SHOW USER DETAILS ---
    @GetMapping("/{id}")
    public String detailUser(@PathVariable Long id, Model model) {
        Optional<CustomUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/detail"; // resources/templates/user/detail.html
        } else {
            return "redirect:/users"; // fallback
        }
    }

    // --- SHOW REGISTER FORM ---
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new CustomUser());
        return "user/register"; // resources/templates/user/register.html
    }

    // --- PROCESS REGISTER FORM ---
    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") CustomUser user) {
        // Encrypt password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Validate role (only ADMIN or REFEREE allowed)
        if (!"ADMIN".equalsIgnoreCase(user.getRole()) && !"REFEREE".equalsIgnoreCase(user.getRole())) {
            user.setRole("REFEREE"); // default role
        }

        userRepo.save(user);
        return "redirect:/login?registered"; // after registering, go to login
    }
}