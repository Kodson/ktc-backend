package com.kodsonApp.repository;

import com.kodsonApp.domain.DailySales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailySalesRepo extends JpaRepository<DailySales, String> {
    Optional<DailySales> findById(String id);
    List<DailySales> findByStation(String station);
}
