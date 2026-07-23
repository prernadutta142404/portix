package com.portix.portix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession;
@Controller
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String register() {
        return "auth/register";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/login";
    }

    @GetMapping("/auth")
    public String auth() {
        return "auth/role-selection";
    }
    
    @GetMapping("/recruiter/register")
    public String recruiterRegister() {
        return "auth/recruiter-register";
    }
    
}