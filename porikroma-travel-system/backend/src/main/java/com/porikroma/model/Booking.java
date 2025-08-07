package com.porikroma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "booking_reference")
    private String bookingReference;
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @Size(max = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_type")
    private BookingType bookingType;
    
    @NotNull
    @Column(name = "booking_date")
    private LocalDateTime bookingDate;
    
    @Column(name = "check_in_date")
    private LocalDateTime checkInDate;
    
    @Column(name = "check_out_date")
    private LocalDateTime checkOutDate;
    
    @NotNull
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Size(max = 3)
    private String currency = "USD";
    
    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status")
    private BookingStatus bookingStatus = BookingStatus.CONFIRMED;
    
    @Column(name = "provider_name")
    @Size(max = 100)
    private String providerName;
    
    @Column(name = "provider_contact")
    @Size(max = 100)
    private String providerContact;
    
    @Lob
    @Column(name = "booking_details")
    private String bookingDetails;
    
    @Lob
    @Column(name = "cancellation_policy")
    private String cancellationPolicy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    public enum BookingType {
        FLIGHT,
        HOTEL,
        TRAIN,
        BUS,
        CAR_RENTAL,
        ACTIVITY,
        RESTAURANT,
        TOUR
    }
    
    public enum BookingStatus {
        CONFIRMED,
        PENDING,
        CANCELLED,
        COMPLETED,
        REFUNDED
    }
}