// File: com/kodsonApp/repository/VariablesRepo.java

package com.kodsonApp.repository;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VariablesRepo extends JpaRepository<Variables, String> {
    // You can add custom query methods here if needed
    List<Variables> findByWayBillNumIn(List<String> wayBillNums);
    List<Variables> findByStatus(String status);
    List<Variables> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
