package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kodsonApp.enumuration.SupplyStatus;
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
@Table(name = "supply")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Supply {

    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotNull(message = "Date is required")
    @Column(nullable = false)
    private Date date;

    @NotBlank(message = "Product is required")
    @Column(nullable = false)
    private String product;

    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Quantity must be positive or zero")
    @Column(nullable = false)
    private Double qty;

    @NotBlank(message = "Station is required")
    @Column(nullable = false)
    private String station;

    @PositiveOrZero(message = "Rate must be positive or zero")
    private Double rate;

    private Double overage;
    private Double shortage;

    @PositiveOrZero(message = "Amount cost must be positive or zero")
    private Double amountCost;

    @PositiveOrZero(message = "Sales rate must be positive or zero")
    private Double salesRate;

    @PositiveOrZero(message = "Amount sales must be positive or zero")
    private Double amountSales;

    @PositiveOrZero(message = "Expected profit must be positive or zero")
    private Double expProfit;

    private String createdBy;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private SupplyStatus status;

    // Approval workflow fields
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String approvalReason;

    private String rejectedBy;
    private LocalDateTime rejectedAt;
    private String rejectionReason;

    // Confirmation workflow fields
    private String confirmedBy;
    private LocalDateTime confirmedAt;
    private Double qtyReceived;
    private String receiptNotes;

    private String comment;
}
