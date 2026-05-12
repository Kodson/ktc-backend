package com.kodsonApp.repository;
import com.kodsonApp.domain.Utility;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilityRepo extends JpaRepository<Utility, String> {
    List<Utility> findByStationId(String stationId);
    List<Utility> findByStatus(String status);
    List<Utility> findByUtility(String utility);
    List<Utility> findByStationIdAndStatus(String stationId, String status);
    Page<Utility> findByStationId(String stationId, Pageable pageable);
}