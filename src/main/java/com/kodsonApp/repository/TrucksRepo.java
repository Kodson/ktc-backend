package com.kodsonApp.repository;

import com.kodsonApp.domain.Trucks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrucksRepo extends JpaRepository<Trucks, String> {
}
