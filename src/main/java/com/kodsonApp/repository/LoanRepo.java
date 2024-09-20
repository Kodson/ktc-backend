package com.kodsonApp.repository;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.PayRoll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepo extends JpaRepository<Loans, String> {
    @Query("SELECT p FROM Loans p WHERE p.employeeId = ?1")
    List<Loans> findByEmployeeId(String employeeId);
}
