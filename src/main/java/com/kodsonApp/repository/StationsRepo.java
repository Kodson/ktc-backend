package com.kodsonApp.repository;

import com.kodsonApp.domain.Stations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StationsRepo extends JpaRepository<Stations, String> {
    @Override
    Optional<Stations> findById(String id);
    Optional<Stations> findByManager(String manager);
    Optional<Stations> findByManagerUserId(String managerUserId);
}
