package com.kodsonApp.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AssignManagerRequest {
    private String userId;
    private ManagerDetails managerDetails;
    private String assignedBy;
}

