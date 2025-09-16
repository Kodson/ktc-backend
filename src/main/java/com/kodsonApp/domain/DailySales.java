package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kodsonApp.enumuration.ValidationStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "daily_sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class DailySales {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotBlank(message = "Station is required")
    @Column(name = "station", nullable = false)
    private String station;

    @Column(name = "date")
    private String date;

    @Column(name = "product")
    private String product;

    @Column(name = "opensl", nullable = false)
    private Double openSL = 0.0;

    @Column(name = "supply")
    private Double supply;

    @Column(name = "overageShortageL")
    private Double overageShortageL;

    @Column(name = "availablel", nullable = false)
    private Double availableL = 0.0;

    @Column(name = "closingsl")
    private Double closingSL;

    @Column(name = "differencel")
    private Double differenceL;

    @Column(name = "checkl", nullable = false)
    private Double checkL = 0.0;

    @Column(name = "opensr")
    private Double openSR;

    @Column(name = "closingsr")
    private Double closingSR;

    @Column(name = "returntt")
    private Double returnTT;

    @Column(name = "salesl")
    private Double salesL;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "value")
    private Double value;

    @Column(name = "cashSales")
    private Double cashSales;

    @Column(name = "creditSales")
    private Double creditSales;

    @Column(name = "advances")
    private Double advances;

    @Column(name = "shortageMomo")
    private Double shortageMomo;

    @Column(name = "cashAvailable")
    private Double cashAvailable;

    @Column(name = "repaymentShortageMomo")
    private Double repaymentShortageMomo;

    @Column(name = "actualCash", nullable = false)
    private Double actualCash = 0.0;

    @Column(name = "bankLodgement", nullable = false)
    private Double bankLodgement = 0.0;

    @Column(name = "cashToBank", nullable = false)
    private Double cashToBank = 0.0;

    @Column(name = "enteredBy")
    private String enteredBy;

    @Column(name = "receivedFromDebtors", nullable = false)
    private Double receivedFromDebtors = 0.0;

    @Column(name = "repaymentAdvances", nullable = false)
    private Double repaymentAdvances = 0.0;

    // Validation and tracking fields
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ValidationStatus status = ValidationStatus.PENDING;

    @Column(name = "validated_by")
    private String validatedBy;

    @Column(name = "approvedBy")
    private String approvedBy;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "validation_notes", columnDefinition = "TEXT")
    private String validationNotes;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = ValidationStatus.PENDING;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }
}
