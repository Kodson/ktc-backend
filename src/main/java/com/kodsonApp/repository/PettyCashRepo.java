package com.kodsonApp.repository;

import com.kodsonApp.domain.PettyCash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PettyCashRepo extends JpaRepository<PettyCash, String> {
        Optional<PettyCash> findById(String id);
        List<PettyCash> findByStatus(String status);
        List<PettyCash> findByStatusAndDateBetweenAndStation(String status, LocalDate startDate, LocalDate endDate, String station,Pageable pageable);

        Page<PettyCash> findByStatusAndUserName(String status, String userName, Pageable pageable);

        Page<PettyCash> findByStatusAndStation(String status, String station, Pageable pageable);

        // Custom search query to filter by multiple fields
        Page<PettyCash> findByCostCenterContainingOrReceiverContainingOrStatusContainingOrRequestDescriptionContainingOrStationContainingOrUserNameContaining(
                String costCenter, String receiver, String status, String description, String station, String userName, Pageable pageable);


}
