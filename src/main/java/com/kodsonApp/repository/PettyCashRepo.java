package com.kodsonApp.repository;

import com.kodsonApp.domain.PettyCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PettyCashRepo extends JpaRepository<PettyCash, String> {
        Optional<PettyCash> findById(String id);
        List<PettyCash> findByStatus(String status);
        List<PettyCash> findByStatusAndDateBetweenAndStation(String status, LocalDate startDate, LocalDate endDate, String station);


        List<PettyCash> findByStatusAndUserName(String status, String userName);

        List<PettyCash> findByStatusAndStation(String status, String station);
}
