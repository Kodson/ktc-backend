package com.kodsonApp.domain;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
public class Statutory {
    @Id
    @UuidGenerator
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", unique = true, updatable = false)
    private String id;

    @NotBlank(message = "Type is required")
    @Column(nullable = false)
    private String type;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Authority is required")
    @Column(nullable = false)
    private String authority;

    @NotBlank(message = "Reference is required")
    @Column(nullable = false)
    private String reference;

    @NotNull(message = "Registered date is required")
    @Column(nullable = false)
    private LocalDate registeredDate;

    @NotNull(message = "Issued date is required")
    @Column(nullable = false)
    private LocalDate issuedDate;

    @NotNull(message = "Expires date is required")
    @Column(nullable = false)
    private LocalDate expiresDate;

    private Integer daysRemaining;

    @NotNull(message = "Fees is required")
    @Column(nullable = false)
    private Double fees;

    @NotBlank(message = "Payment status is required")
    @Column(nullable = false)
    private String paymentStatus;

    @NotBlank(message = "Status is required")
    @Column(nullable = false)
    private String status;

    @NotBlank(message = "Assignee is required")
    @Column(nullable = false)
    private String assignee;

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
