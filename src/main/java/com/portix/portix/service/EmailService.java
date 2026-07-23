package com.portix.portix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendOtpEmail(String recipientEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(recipientEmail);
        message.setSubject("Your PORTIX Verification Code");

        message.setText(
                "Welcome to PORTIX!\n\n" +
                "Your email verification code is: " + otp + "\n\n" +
                "This OTP is valid for 5 minutes.\n\n" +
                "If you did not request this verification, please ignore this email.\n\n" +
                "Regards,\n" +
                "PORTIX Team"
        );

        mailSender.send(message);
    }
}