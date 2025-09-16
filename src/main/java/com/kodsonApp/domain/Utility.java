package com.kodsonApp.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(NON_DEFAULT)
public class Utility {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotNull(message = "Due date is required")
    @Column(nullable = false)
    private LocalDate dueDate;

    private Integer daysOverdue;

    @NotBlank(message = "Utility type is required")
    @Column(nullable = false)
    private String utility;

    @NotBlank(message = "Provider is required")
    @Column(nullable = false)
    private String provider;

    @NotBlank(message = "Bill number is required")
    @Column(nullable = false)
    private String billNumber;

    private String period;

    @Embedded
    private Consumption consumption;

    @NotNull(message = "Amount is required")
    @Column(nullable = false)
    private Double amount;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @NotBlank(message = "Priority is required")
    @Column(nullable = false)
    private String priority;

    @NotBlank(message = "Station ID is required")
    @Column(nullable = false)
    private String stationId;

    @NotBlank(message = "Station name is required")
    @Column(nullable = false)
    private String stationName;

    @NotBlank(message = "Created by is required")
    @Column(nullable = false)
    private String createdBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
