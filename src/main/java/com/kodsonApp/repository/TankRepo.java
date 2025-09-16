package com.kodsonApp.repository;

import com.kodsonApp.domain.Tank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TankRepo extends JpaRepository<Tank, String> {
    // Additional custom queries can be added here if needed
    Optional<Tank> findByStationAndFuelType(String station, String fuelType);
}
