package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "tank")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Tank {

    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private Date date;

    @NotBlank(message = "Product is required")
    @Column(nullable = false)
    private String fuelType;

    @NotBlank(message = "tank name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "capacity is required")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    @Column(nullable = false)
    private Double capacity;

    @NotNull(message = "current stock is required")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    @Column(nullable = false)
    private Double currentStock;

    @NotBlank(message = "Station is required")
    @Column(nullable = false)
    private String station;

    @PositiveOrZero(message = "Rate must be positive or zero")
    private Double pricePerLiter;

    private Double fillPercentage;
    private Double reorderThreshold;

    private Double minLevel;
    private Double maxLevel;
    private boolean autoReorder;

    @CreationTimestamp
    @Column(name = "last refill")
    private LocalDateTime lastRefill;

    private String createdBy;

    private String reason;
    private String type;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private TankStatus status;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    // Enum for status values
    public enum TankStatus {
        Good,
        Critical,
        Low,
        Active,
        Maintenance,
        Inactive,
    }
}
