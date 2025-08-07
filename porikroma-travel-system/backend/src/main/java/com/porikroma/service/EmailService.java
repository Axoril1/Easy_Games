package com.porikroma.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Value("${app.baseUrl:http://localhost:8080}")
    private String baseUrl;
    
    public void sendVerificationEmail(String toEmail, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Porikroma - Verify Your Email Address");
            
            String verificationUrl = baseUrl + "/api/auth/verify-email?token=" + verificationToken;
            String text = "Welcome to Porikroma!\n\n" +
                         "Please click the following link to verify your email address:\n" +
                         verificationUrl + "\n\n" +
                         "If you didn't create an account with Porikroma, please ignore this email.\n\n" +
                         "Best regards,\n" +
                         "The Porikroma Team";
            
            message.setText(text);
            mailSender.send(message);
            
            log.info("Verification email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send verification email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send verification email", e);
        }
    }
    
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Porikroma - Password Reset Request");
            
            String resetUrl = baseUrl + "/reset-password?token=" + resetToken;
            String text = "Hello,\n\n" +
                         "We received a request to reset your password for your Porikroma account.\n\n" +
                         "Please click the following link to reset your password:\n" +
                         resetUrl + "\n\n" +
                         "This link will expire in 1 hour for security reasons.\n\n" +
                         "If you didn't request a password reset, please ignore this email.\n\n" +
                         "Best regards,\n" +
                         "The Porikroma Team";
            
            message.setText(text);
            mailSender.send(message);
            
            log.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }
    
    public void sendWelcomeEmail(String toEmail, String firstName) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Porikroma - Your Travel Companion Awaits!");
            
            String text = "Dear " + firstName + ",\n\n" +
                         "Welcome to Porikroma! We're excited to have you join our community of travelers.\n\n" +
                         "With Porikroma, you can:\n" +
                         "• Plan solo adventures or organize group trips\n" +
                         "• Get AI-powered destination recommendations\n" +
                         "• Track your budget and expenses\n" +
                         "• Book flights, hotels, and activities\n" +
                         "• Stay safe with our emergency features\n" +
                         "• Connect with fellow travelers\n\n" +
                         "Start planning your next adventure today!\n\n" +
                         "Happy travels,\n" +
                         "The Porikroma Team";
            
            message.setText(text);
            mailSender.send(message);
            
            log.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
        }
    }
}