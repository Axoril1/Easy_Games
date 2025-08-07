package com.porikroma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "travel_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TravelGroup {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String name;
    
    @Size(max = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "group_purpose")
    private GroupPurpose groupPurpose;
    
    @Column(name = "max_members")
    private Integer maxMembers = 50;
    
    @Column(name = "is_public")
    private Boolean isPublic = false;
    
    @Column(name = "invite_code")
    @Size(max = 10)
    private String inviteCode;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "group_members",
        joinColumns = @JoinColumn(name = "group_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> members = new HashSet<>();
    
    @OneToMany(mappedBy = "travelGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GroupRole> groupRoles = new HashSet<>();
    
    @OneToMany(mappedBy = "travelGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Trip> trips = new HashSet<>();
    
    @OneToMany(mappedBy = "travelGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GroupMessage> messages = new HashSet<>();
    
    @OneToMany(mappedBy = "travelGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<GroupPoll> polls = new HashSet<>();
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum GroupPurpose {
        FRIENDS_TRIP,
        FAMILY_VACATION,
        STUDY_TOUR,
        BUSINESS_TRAVEL,
        ADVENTURE_GROUP,
        CULTURAL_EXCHANGE,
        VOLUNTEER_TRAVEL,
        SPORTS_TRAVEL,
        PHOTOGRAPHY_TOUR,
        FOOD_TOUR
    }
}