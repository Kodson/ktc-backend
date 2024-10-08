package com.kodsonApp.repository;

import com.kodsonApp.domain.ItemRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemRequestRepo extends JpaRepository<ItemRequest, String> {
    Optional<ItemRequest> findById(String id);
    List<ItemRequest> findByStatus(String status);
    List<ItemRequest> findByStatusAndDateBetween(String status, LocalDate startDate, LocalDate endDate);
}
