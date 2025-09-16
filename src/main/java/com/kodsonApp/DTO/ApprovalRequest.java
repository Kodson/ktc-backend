package com.kodsonApp.DTO;

import com.kodsonApp.enumuration.ValidationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ApprovalRequest {

    @NotBlank(message = "Approved by field is required")
    private String approvedBy;

    private String reason;

    @NotNull(message = "Validation status is required")
    private ValidationStatus status;

    private String notes;
}
