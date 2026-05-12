package com.kodsonApp.repository;

import com.kodsonApp.domain.Statutory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StatutoryRepo extends JpaRepository<Statutory, String> {
    List<Statutory> findByStationId(String stationId);
    Page<Statutory> findByStationId(String stationId, Pageable pageable);
    List<Statutory> findByType(String type);
    List<Statutory> findByPaymentStatus(String paymentStatus);
    List<Statutory> findByStationIdAndPaymentStatus(String stationId, String paymentStatus);
    List<Statutory> findByExpiresDateBefore(LocalDate date);
    List<Statutory> findByExpiresDateBetween(LocalDate startDate, LocalDate endDate);
}
