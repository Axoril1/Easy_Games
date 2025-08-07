package com.porikroma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @NotBlank
    @Size(max = 2000)
    private String content;
    
    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "review_type")
    private ReviewType reviewType;
    
    @Size(max = 100)
    @Column(name = "subject_name")
    private String subjectName; // Name of hotel, restaurant, destination, etc.
    
    @Size(max = 200)
    @Column(name = "subject_location")
    private String subjectLocation;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "helpful_count")
    private Integer helpfulCount = 0;
    
    @Column(name = "is_anonymous")
    private Boolean isAnonymous = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum ReviewType {
        DESTINATION,
        HOTEL,
        RESTAURANT,
        ACTIVITY,
        TRANSPORT_SERVICE,
        LOCAL_SERVICE,
        OVERALL_TRIP
    }
}