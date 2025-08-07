package com.porikroma.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expense {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 100)
    private String title;
    
    @Size(max = 500)
    private String description;
    
    @NotNull
    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
    
    @Size(max = 3)
    private String currency = "USD";
    
    @NotNull
    @Column(name = "expense_date")
    private LocalDate expenseDate;
    
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;
    
    @Lob
    @Column(name = "receipt_image")
    private String receiptImage;
    
    @Column(name = "is_shared_expense")
    private Boolean isSharedExpense = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    public enum ExpenseCategory {
        TRANSPORTATION,
        ACCOMMODATION,
        FOOD,
        ACTIVITIES,
        SHOPPING,
        EMERGENCY,
        MISCELLANEOUS
    }
}