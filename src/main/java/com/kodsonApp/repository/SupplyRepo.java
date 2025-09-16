package com.kodsonApp.repository;

import com.kodsonApp.domain.Supply;
import com.kodsonApp.enumuration.SupplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplyRepo extends JpaRepository<Supply,String> {
    Optional<Supply> findById(String id);
    List<Supply> findByStation(String station);
    Page<Supply> findByStation(String station, Pageable pageable);
    List<Supply> findByStatus(SupplyStatus status);
    Optional<Supply> findByStationAndProductAndCreatedAtBetween(String station, String product, java.time.LocalDateTime start, java.time.LocalDateTime end);
    Optional<Supply> findByStationAndProductAndDate(String station, String product, Date date);
    List<Supply> findByStationAndProductAndDateBetween(String station, String product, Date start, Date end);
}
