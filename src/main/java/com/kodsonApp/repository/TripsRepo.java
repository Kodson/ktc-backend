package com.kodsonApp.repository;

import com.kodsonApp.domain.Trips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripsRepo extends JpaRepository<Trips, String> {
    List<Trips> findByBrvAndDateReceived(String brv, LocalDate dateReceived);

    List<Trips> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
