package com.kodsonApp.domain;

import com.kodsonApp.enumuration.TankOperation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "tank_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TankHistory {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    @Column(name = "tank_id", nullable = false)
    private String tankId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TankOperation operation;

    @Column(name = "quantity")
    private Double quantity;

    @Column(name = "previous_level")
    private Double previousLevel;

    @Column(name = "new_level")
    private Double newLevel;

    @Column(name = "performed_by")
    private String performedBy;

    @Column(name = "performed_at", nullable = false)
    private LocalDateTime performedAt;

    @Column(name = "notes")
    private String notes;

    @PrePersist
    protected void onCreate() {
        performedAt = LocalDateTime.now();
    }
}
