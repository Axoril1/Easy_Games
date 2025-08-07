package com.porikroma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private String profilePicture;
    private String bio;
    private TravelPreference travelPreference;
    private String emergencyContactName;
    private String emergencyContactPhone;
    private String emergencyContactEmail;
    private Boolean isEmailVerified;
    private Boolean isOauthUser;
    private String preferredCurrency;
    private String preferredLanguage;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> roles;
    
    public enum TravelPreference {
        ADVENTURE,
        LEISURE,
        BUSINESS,
        CULTURAL,
        ROMANTIC,
        FAMILY,
        BACKPACKING,
        LUXURY,
        ECO_TOURISM,
        FOOD_TOURISM
    }
    
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return username;
    }
}