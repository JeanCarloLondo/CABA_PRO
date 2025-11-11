package com.cabapro.development.controller;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class UserAdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserAdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users/create")
    public String createAdmin(@RequestParam("name") String name,
                              @RequestParam("email") String email,
                              @RequestParam("password") String password,
                              Model model) {

        try {
            validateUserInput(name, email, password);
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("user", new CustomUser());
            return "admin/users/form"; // vuelve al formulario con mensaje de error
        }

        CustomUser admin = new CustomUser();
        admin.setName(name.trim());
        admin.setEmail(email.trim());
        admin.setPassword(passwordEncoder.encode(password));
        admin.setRole("ADMIN");

        userRepository.save(admin);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new CustomUser());
        return "admin/users/form";
    }

    private void validateUserInput(String name, String email, String password) {
        if (name == null || email == null || password == null)
            throw new IllegalArgumentException("All fields are required.");

        // No spaces allowed anywhere
        if (name.matches(".*\\s.*"))
            throw new IllegalArgumentException("Name cannot contain spaces.");
        if (email.matches(".*\\s.*"))
            throw new IllegalArgumentException("Email cannot contain spaces.");
        if (password.matches(".*\\s.*"))
            throw new IllegalArgumentException("Password cannot contain spaces.");

        // Email format
        if (!email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
            throw new IllegalArgumentException("Invalid email format.");

        // Minimum length for password
        if (password.length() < 8)
            throw new IllegalArgumentException("Password must be at least 8 characters long.");
    }
}