package com.kodsonApp.repository;

import com.kodsonApp.domain.CreditSales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditSalesRepo extends JpaRepository<CreditSales, String> {
    Optional<CreditSales> findById(String id);
    List<CreditSales> findByStation(String station);
}
