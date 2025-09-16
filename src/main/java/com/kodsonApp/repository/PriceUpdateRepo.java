package com.kodsonApp.repository;

import com.kodsonApp.domain.PriceUpdate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceUpdateRepo extends JpaRepository<PriceUpdate, String> {
}
