package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "redirect:/login.html";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "redirect:/signup.html";
    }

    @GetMapping("/home")
    public String homePage() {
        return "redirect:/home.html";
    }
}
