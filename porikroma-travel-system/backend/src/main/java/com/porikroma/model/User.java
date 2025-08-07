package com.porikroma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(unique = true)
    private String username;
    
    @NotBlank
    @Email
    @Size(max = 100)
    @Column(unique = true)
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
    
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;
    
    @Size(max = 15)
    private String phoneNumber;
    
    @Lob
    private String profilePicture;
    
    @Size(max = 500)
    private String bio;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "travel_preference")
    private TravelPreference travelPreference;
    
    @Column(name = "emergency_contact_name")
    @Size(max = 100)
    private String emergencyContactName;
    
    @Column(name = "emergency_contact_phone")
    @Size(max = 15)
    private String emergencyContactPhone;
    
    @Column(name = "emergency_contact_email")
    @Email
    @Size(max = 100)
    private String emergencyContactEmail;
    
    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;
    
    @Column(name = "email_verification_token")
    private String emailVerificationToken;
    
    @Column(name = "password_reset_token")
    private String passwordResetToken;
    
    @Column(name = "password_reset_expires_at")
    private LocalDateTime passwordResetExpiresAt;
    
    @Column(name = "google_id")
    private String googleId;
    
    @Column(name = "is_oauth_user")
    private Boolean isOauthUser = false;
    
    @Column(name = "preferred_currency")
    @Size(max = 3)
    private String preferredCurrency = "USD";
    
    @Column(name = "preferred_language")
    @Size(max = 5)
    private String preferredLanguage = "en";
    
    @Column(name = "notification_preferences")
    private String notificationPreferences;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Trip> trips = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Expense> expenses = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Review> reviews = new HashSet<>();
    
    @ManyToMany(mappedBy = "members", fetch = FetchType.LAZY)
    private Set<TravelGroup> travelGroups = new HashSet<>();
    
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
}