package com.kodsonApp.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SupplyConfirmationRequest {

    @NotBlank(message = "Supply ID is required")
    private String id;

    @NotBlank(message = "Confirmed by is required")
    private String confirmedBy;

    @NotNull(message = "Confirmed at date is required")
    private LocalDateTime confirmedAt;

    @NotNull(message = "Received quantity is required")
    @Positive(message = "Received quantity must be positive")
    private Double qtyR;

    private Double overage;

    private Double shortage;

    private String notes;
}
