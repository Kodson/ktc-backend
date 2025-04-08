package com.kodsonApp.repository;

import com.kodsonApp.domain.Attendants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendantsRepo extends JpaRepository<Attendants, String> {
    Optional<Attendants> findById(String id);
    List<Attendants> findByStation(String station);
}