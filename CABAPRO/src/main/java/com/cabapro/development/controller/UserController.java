package com.cabapro.development.controller;

import com.cabapro.development.model.CustomUser;
import com.cabapro.development.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    //User list
    @GetMapping
    public String listUsers(Model model) {
        List<CustomUser> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "user/list"; // resources/templates/user/list.html
    }

    //  show user details
    @GetMapping("/{id}")
    public String detailUser(@PathVariable Long id, Model model) {
        Optional<CustomUser> user = userRepo.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "user/detail"; // resources/templates/user/detail.html
        } else {
            return "redirect:/users"; // if it doesn't exist, redirect to user list
        }
    }
}
