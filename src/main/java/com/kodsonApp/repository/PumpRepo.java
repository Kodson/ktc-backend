package com.kodsonApp.repository;

import com.kodsonApp.domain.Pump;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PumpRepo extends JpaRepository<Pump, String> {
    Optional<Pump> findById(String id);
    List<Pump> findByStation(String station);
}
