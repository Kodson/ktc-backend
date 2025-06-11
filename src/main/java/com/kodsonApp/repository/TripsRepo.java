package com.kodsonApp.repository;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TripsRepo extends JpaRepository<Trips, String> {
    List<Trips> findByBrvAndDateReceived(String brv, LocalDate dateReceived);

    Page<Trips> findByDateBetween(LocalDate startDate, LocalDate endDate,Pageable pageable);
    Page<Trips> findByBrvContainingOrWayBillNumContainingOrBvoContaining(
            String brv, String wayBillNum, String bvo, Pageable pageable);

    @Query("SELECT COALESCE(MAX(t.sequence), 0) FROM Trips t")
    long findMaxSequence();
}
