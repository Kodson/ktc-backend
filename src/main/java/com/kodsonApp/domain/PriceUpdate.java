package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Table(name = "price_updates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class PriceUpdate {
    @Id
    @UuidGenerator
    @Column(name = "id", unique = true, updatable = false)
    private String id;
    private String updateScope;
    private UUID targetTankId;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "target_station")
    private List<String> targetStation;
    private String fuelType;
    private double newPrice;
    private LocalDateTime effectiveDate;
    private String reason;
    private String updatedBy;
    private double currentPrice;
    private double percentageChange;
    private double priceDifference;
    private int totalAffectedTanks;
    private String status; // pending, approved, rejected
    private String approvalComment;
    private String approvedBy; // User ID of the approver
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "affected_tank_ids")
    private List<String> affectedTankIds;

    private LocalDateTime createdAt = LocalDateTime.now();

}
