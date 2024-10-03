package com.kodsonApp.repository;

import com.kodsonApp.domain.MedicalBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MedicalBillRepo extends JpaRepository<MedicalBill, String> {
    // Custom query to get medical bills between two dates
    List<MedicalBill> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
