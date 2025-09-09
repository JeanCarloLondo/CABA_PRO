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
    public String createReferee(@RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("password") String password) {

        CustomUser referee = new CustomUser();
        referee.setName(name);
        referee.setEmail(email);
        referee.setPassword(passwordEncoder.encode(password));
        referee.setRole("REFEREE"); // Set role to REFEREE

        userRepository.save(referee);

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/users/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new CustomUser());
        return "admin/users/form"; // templates/admin/users/form.html
    }
}