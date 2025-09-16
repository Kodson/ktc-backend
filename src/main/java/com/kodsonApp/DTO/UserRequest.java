package com.kodsonApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String username;
    private String email;
    private String role;
    private boolean isActive;
    private boolean isNonLocked;
    private String phone;
    private String password;
    private String status;
    private Long id;
}
