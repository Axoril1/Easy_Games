package com.porikroma.controller;

import com.porikroma.dto.*;
import com.porikroma.model.Role;
import com.porikroma.model.User;
import com.porikroma.repository.RoleRepository;
import com.porikroma.repository.UserRepository;
import com.porikroma.security.JwtUtils;
import com.porikroma.security.UserPrincipal;
import com.porikroma.service.EmailService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private PasswordEncoder encoder;
    
    @Autowired
    private JwtUtils jwtUtils;
    
    @Autowired
    private EmailService emailService;
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        
        String refreshToken = jwtUtils.generateRefreshToken(userDetails.getUsername());
        
        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken, userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(),
                "", "", roles)); // firstName and lastName can be fetched separately
    }
    
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }
        
        // Create new user's account
        User user = new User();
        user.setUsername(signUpRequest.getUsername());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(encoder.encode(signUpRequest.getPassword()));
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setTravelPreference(signUpRequest.getTravelPreference());
        user.setEmergencyContactName(signUpRequest.getEmergencyContactName());
        user.setEmergencyContactPhone(signUpRequest.getEmergencyContactPhone());
        user.setEmergencyContactEmail(signUpRequest.getEmergencyContactEmail());
        user.setPreferredCurrency(signUpRequest.getPreferredCurrency());
        user.setPreferredLanguage(signUpRequest.getPreferredLanguage());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        
        // Generate email verification token
        String verificationToken = UUID.randomUUID().toString();
        user.setEmailVerificationToken(verificationToken);
        
        // Assign default role
        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        user.setRoles(Set.of(userRole));
        
        userRepository.save(user);
        
        // Send verification email
        try {
            emailService.sendVerificationEmail(user.getEmail(), verificationToken);
        } catch (Exception e) {
            log.error("Failed to send verification email: {}", e.getMessage());
        }
        
        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email for verification."));
    }
    
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElse(null);
        
        if (user == null) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid verification token!"));
        }
        
        user.setIsEmailVerified(true);
        user.setEmailVerificationToken(null);
        userRepository.save(user);
        
        return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElse(null);
        
        if (user == null) {
            // Don't reveal if email exists or not for security
            return ResponseEntity.ok(new MessageResponse("If the email exists, a password reset link has been sent."));
        }
        
        // Generate password reset token
        String resetToken = UUID.randomUUID().toString();
        user.setPasswordResetToken(resetToken);
        user.setPasswordResetExpiresAt(LocalDateTime.now().plusHours(1)); // Token expires in 1 hour
        userRepository.save(user);
        
        // Send password reset email
        try {
            emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
        } catch (Exception e) {
            log.error("Failed to send password reset email: {}", e.getMessage());
        }
        
        return ResponseEntity.ok(new MessageResponse("If the email exists, a password reset link has been sent."));
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userRepository.findByPasswordResetToken(request.getToken())
                .orElse(null);
        
        if (user == null || user.getPasswordResetExpiresAt().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Invalid or expired reset token!"));
        }
        
        user.setPassword(encoder.encode(request.getNewPassword()));
        user.setPasswordResetToken(null);
        user.setPasswordResetExpiresAt(null);
        userRepository.save(user);
        
        return ResponseEntity.ok(new MessageResponse("Password reset successfully!"));
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        
        if (jwtUtils.validateJwtToken(requestRefreshToken)) {
            String username = jwtUtils.getUsernameFromJwtToken(requestRefreshToken);
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            String newToken = jwtUtils.generateJwtToken(username, user.getId(), user.getEmail());
            
            return ResponseEntity.ok(new TokenRefreshResponse(newToken, requestRefreshToken));
        }
        
        return ResponseEntity.badRequest()
                .body(new MessageResponse("Invalid refresh token!"));
    }
}