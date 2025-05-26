// File: com/kodsonApp/repository/VariablesRepo.java

package com.kodsonApp.repository;

import com.kodsonApp.domain.Shortages;
import com.kodsonApp.domain.Surcharge;
import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ShortageRepo extends JpaRepository<Shortages, String> {
    Page<Shortages> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    Page<Shortages> findByBrvContainingOrWayBillNumContainingOrBvoContainingOrMonthContaining(
            String brv, String wayBillNum, String bvo, String month,  Pageable pageable);

    @Query("SELECT p FROM Shortages p WHERE p.employeeId = ?1")
    List<Shortages> findByEmployeeId(String employeeId);
}
