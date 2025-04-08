package com.kodsonApp.repository;

import com.kodsonApp.domain.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankRepo extends JpaRepository<Bank, String> {
    @Override
    Optional<Bank> findById(String id);
    List<Bank> findByStation(String station);
}
