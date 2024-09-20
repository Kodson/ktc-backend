package com.kodsonApp.repository;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.Surcharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurchargeRepo extends JpaRepository<Surcharge, String> {
    @Query("SELECT p FROM Surcharge p WHERE p.employeeId = ?1")
    List<Surcharge> findByEmployeeId(String employeeId);
}
