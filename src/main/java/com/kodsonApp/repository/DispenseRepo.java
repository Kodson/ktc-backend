package com.kodsonApp.repository;

import com.kodsonApp.domain.Dispense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DispenseRepo extends JpaRepository<Dispense, String> {
    // JpaRepository provides built-in CRUD methods like:
    // - findAll(): To fetch all records
    // - findById(): To fetch a single record by ID
    // - save(): To create or update a record
    // - deleteById(): To delete a record by ID
}
