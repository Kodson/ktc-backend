package com.kodsonApp.repository;

import com.kodsonApp.domain.FuelDischarge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FuelDischargeRepo extends JpaRepository<FuelDischarge, String> {
}
