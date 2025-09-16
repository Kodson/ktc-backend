package com.kodsonApp.repository;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.enumuration.ValidationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DailySalesRepo extends JpaRepository<DailySales, String> {
    Optional<DailySales> findById(String id);
    Page<DailySales> findByStation(String station, Pageable pageable);

    List<DailySales> findByStatus(ValidationStatus status);
    List<DailySales> findByStationAndStatus(String station, ValidationStatus status);
    List<DailySales> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<DailySales> findByStationAndCreatedAtBetween(String station, LocalDateTime startDate, LocalDateTime endDate);
    List<DailySales> findByValidatedBy(String validatedBy);
    boolean existsByStationAndCreatedAtBetween(String station, LocalDateTime startDate, LocalDateTime endDate);
    Optional<DailySales> findFirstByStationAndProductOrderByCreatedAtDesc(String station, String product);
    List<DailySales> findByStationAndProductOrderByCreatedAtDesc(String station, String product);
}
