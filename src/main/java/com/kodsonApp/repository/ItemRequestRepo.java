package com.kodsonApp.repository;

import com.kodsonApp.domain.ItemRequest;
import com.kodsonApp.domain.PettyCash;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, String> {
    Optional<ItemRequest> findById(String id);
    List<ItemRequest> findByStatus(String status);
   // List<ItemRequest> findByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate);
    Page<ItemRequest> findByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate,  Pageable pageable);
}
