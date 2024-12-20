// File: com/kodsonApp/repository/VariablesRepo.java

package com.kodsonApp.repository;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VariablesRepo extends JpaRepository<Variables, String> {
    // You can add custom query methods here if needed
    //List<Variables> findByWayBillNumIn(List<String> wayBillNums);
    //List<Variables> findByStatus(String status);
    Page<Variables> findByStatus(String status, Pageable pageable);
    //List<Variables> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Page<Variables> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Variables> findByBrvContainingOrWayBillNumContainingOrBvoContainingOrMonthContainingOrSubCompanyContaining(
            String brv, String wayBillNum, String bvo, String month, String subCompany, Pageable pageable);
}
