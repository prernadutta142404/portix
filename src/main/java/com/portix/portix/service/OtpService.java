package com.portix.portix.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

    @Autowired
    private EmailService emailService;

    private final SecureRandom random = new SecureRandom();

    private final Map<String, String> otpStorage =
            new ConcurrentHashMap<>();

    private final Map<String, LocalDateTime> otpExpiry =
            new ConcurrentHashMap<>();
    private final Map<String, Boolean> verifiedEmails =
            new ConcurrentHashMap<>();

    public void generateAndSendOtp(String email) {

        // Generate secure 6-digit OTP
        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        // Store OTP temporarily
        otpStorage.put(email, otp);

        // OTP expires after 5 minutes
        otpExpiry.put(
                email,
                LocalDateTime.now().plusMinutes(5)
        );

        // Send OTP to user's email
        emailService.sendOtpEmail(email, otp);
    }

    public boolean verifyOtp(String email, String otp) {

        String storedOtp = otpStorage.get(email);

        LocalDateTime expiry =
                otpExpiry.get(email);

        if (storedOtp == null || expiry == null) {
            return false;
        }
        
        

        // Check expiry
        if (LocalDateTime.now().isAfter(expiry)) {

            otpStorage.remove(email);
            otpExpiry.remove(email);

            return false;
        }
        

        // Check OTP
        if (!storedOtp.equals(otp)) {
            return false;
        }

        // OTP successfully used — remove it
        verifiedEmails.put(email, true);

        otpStorage.remove(email);
        otpExpiry.remove(email);

        return true;
    }
    public boolean isEmailVerified(String email) {
        return verifiedEmails.getOrDefault(email, false);
    }

    public void clearVerification(String email) {
        verifiedEmails.remove(email);
    }
    
}