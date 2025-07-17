package com.kodsonApp.repository;

import com.kodsonApp.domain.NpaPrice;
import com.kodsonApp.domain.Supply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NpaPriceRepo extends JpaRepository<NpaPrice,String> {
    Optional<NpaPrice> findById(String id);
}
