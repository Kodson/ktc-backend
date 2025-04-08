package com.kodsonApp.repository;

import com.kodsonApp.domain.Bdc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BdcRepo extends JpaRepository<Bdc, String> {
    Optional<Bdc> findById(String id);
}
