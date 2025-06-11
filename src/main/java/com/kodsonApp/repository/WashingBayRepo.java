package com.kodsonApp.repository;
import com.kodsonApp.domain.Utility;
import com.kodsonApp.domain.WashingBay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WashingBayRepo extends JpaRepository<WashingBay, String> {
    List<WashingBay> findByStation(String station);
}