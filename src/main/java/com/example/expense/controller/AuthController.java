package com.example.expense.controller;

import com.example.expense.model.User;
import com.example.expense.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (user.getUsername()==null || user.getPassword()==null || user.getEmail()==null) {
            model.addAttribute("error", "All fields are required");
            return "register";
        }
        var exists = userService.findByUsername(user.getUsername());
        if (exists != null) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        userService.register(user);
        return "redirect:/login?registered";
    }
}
