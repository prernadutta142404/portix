package com.portix.portix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.portix.portix.entity.User;
import com.portix.portix.service.UserService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        try {
            User registeredUser = userService.registerUser(user);
            return ResponseEntity.ok(registeredUser);

        } catch (RuntimeException e) {

            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public User login(
            @RequestBody User user,
            @RequestParam String role,
            HttpSession session) {

        User loggedInUser =
                userService.loginUser(
                        user.getEmail(),
                        user.getPassword()
                );

        // Invalid email or password
        if (loggedInUser == null) {
            return null;
        }

        // Selected role must match database role
        if (loggedInUser.getRole() == null
                || !loggedInUser.getRole().equalsIgnoreCase(role)) {

            return null;
        }

        // Create session only after role verification
        session.setAttribute(
                "loggedInUserId",
                loggedInUser.getId()
        );

        session.setAttribute(
                "loggedInUserRole",
                loggedInUser.getRole()
        );

        session.setAttribute(
                "loggedInUser",
                loggedInUser
        );

        return loggedInUser;
    }
}