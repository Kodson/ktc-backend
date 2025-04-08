package com.kodsonApp.repository;
import com.kodsonApp.domain.Distribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DistributionRepo extends JpaRepository<Distribution, String> {
    Optional<Distribution> findById(String id);
    List<Distribution> findByStation(String station);
}
