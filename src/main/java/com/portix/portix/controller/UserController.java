package com.portix.portix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.portix.portix.entity.User;
import com.portix.portix.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import com.portix.portix.service.OtpService;



@RestController
@RequestMapping("/api/users")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private OtpService otpService;
    
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        try {

            otpService.generateAndSendOtp(email);

            return ResponseEntity.ok(
                    "OTP sent successfully"
            );

        } 
        
        catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam String email,
            @RequestParam String otp) {

        boolean isValid = otpService.verifyOtp(email, otp);

        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        }

        return ResponseEntity
                .badRequest()
                .body("Invalid or expired OTP");
    }

    @PostMapping("/register")
    
    public ResponseEntity<?> register(@RequestBody User user) {
    	if (!otpService.isEmailVerified(user.getEmail())) {
    	    return ResponseEntity
    	            .badRequest()
    	            .body("Please verify your email before registration.");
    	}
        try {

            User registeredUser =
                    userService.registerUser(user);
            otpService.clearVerification(user.getEmail());
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
        if (loggedInUser.getRole() == null ||
            !loggedInUser.getRole().equalsIgnoreCase(role)) {

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