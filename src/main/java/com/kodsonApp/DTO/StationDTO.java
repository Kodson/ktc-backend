package com.kodsonApp.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private String id;
    private String name;
    private String code;

    private Location location;
    private Contact contact;
    private Operational operational;
    private Financial financial;
    private User user;

    private String createdBy;
    private LocalDateTime createdAt;
    private String lastModifiedBy;
    private LocalDateTime lastModifiedAt;
    private String notes;

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Location {
        private String address;
        private String city;
        private String region;
        private GpsCoordinates gpsCoordinates;

        @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
        public static class GpsCoordinates {
            private Double latitude;
            private Double longitude;
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Contact {
        private String phone;
        private String email;
        private Manager manager;

        @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
        public static class Manager {
            private String name;
            private String phone;
            private String email;
            private String userId;
        }
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Operational {
        private String status;
        private Map<String, String> operatingHours;
        private List<String> fuelTypes;
        private Map<String, Integer> tankCapacity;
        private int pumpCount;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Financial {
        private double monthlyTarget;
        private Double commissionRate;
        private Double securityDeposit;
        private String lastAuditDate;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class User {
        private String id;
        private String username;
        private String email;
        private String role;
        private String status;
        private String lastLogin;
        private boolean passwordChanged;
        private boolean mustChangePassword;
        private boolean accountLocked;
        private int loginAttempts;
        private String createdAt;
        private String lastModifiedAt;
    }
}
