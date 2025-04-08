package com.kodsonApp.repository;

import com.kodsonApp.domain.Expenses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExpensesRepo extends JpaRepository<Expenses, String> {
    Optional<Expenses> findById(String id);
    List<Expenses> findByStation(String station);
}
