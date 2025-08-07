package com.porikroma.dto;

import com.porikroma.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SignupRequest {
    
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;
    
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;
    
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    
    @NotBlank
    @Size(max = 50)
    private String firstName;
    
    @NotBlank
    @Size(max = 50)
    private String lastName;
    
    @Past
    private LocalDate dateOfBirth;
    
    @Size(max = 15)
    private String phoneNumber;
    
    private User.TravelPreference travelPreference;
    
    @Size(max = 100)
    private String emergencyContactName;
    
    @Size(max = 15)
    private String emergencyContactPhone;
    
    @Email
    @Size(max = 100)
    private String emergencyContactEmail;
    
    @Size(max = 3)
    private String preferredCurrency = "USD";
    
    @Size(max = 5)
    private String preferredLanguage = "en";
}