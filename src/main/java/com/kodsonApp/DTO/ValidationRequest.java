package com.kodsonApp.DTO;

import com.kodsonApp.enumuration.ValidationStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationRequest {

    @NotNull(message = "Validation status is required")
    private ValidationStatus status;

    @NotBlank(message = "Validated by field is required")
    private String validatedBy;

    private String notes;
}
