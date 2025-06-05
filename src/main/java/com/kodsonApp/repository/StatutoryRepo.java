package com.kodsonApp.repository;

import com.kodsonApp.domain.Statutory;
import com.kodsonApp.domain.Utility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StatutoryRepo extends JpaRepository<Statutory, String> {
    List<Statutory> findByStation(String station);
}
