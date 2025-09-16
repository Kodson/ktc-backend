package com.kodsonApp.repository;

import com.kodsonApp.domain.Statutory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatutoryRepo extends JpaRepository<Statutory, String> {
    List<Statutory> findByStationId(String stationId);
    List<Statutory> findByStatus(String status);
    List<Statutory> findByType(String type);
    List<Statutory> findByPaymentStatus(String paymentStatus);
    List<Statutory> findByStationIdAndStatus(String stationId, String status);
    List<Statutory> findByExpiresDateBefore(LocalDate date);
    List<Statutory> findByExpiresDateBetween(LocalDate startDate, LocalDate endDate);
}
