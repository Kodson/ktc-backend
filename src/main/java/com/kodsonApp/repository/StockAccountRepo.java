package com.kodsonApp.repository;

import com.kodsonApp.domain.StockAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface StockAccountRepo extends JpaRepository<StockAccount, String> {
    Optional<StockAccount> findById(String id);
    List<StockAccount> findByStation(String station);
}