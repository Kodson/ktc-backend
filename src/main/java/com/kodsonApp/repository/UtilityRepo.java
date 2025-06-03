package com.kodsonApp.repository;
import com.kodsonApp.domain.Utility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilityRepo extends JpaRepository<Utility, String> {
    List<Utility> findByStation(String station);
}