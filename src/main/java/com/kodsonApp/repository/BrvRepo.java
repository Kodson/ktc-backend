package com.kodsonApp.repository;

import com.kodsonApp.domain.Brv;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BrvRepo extends JpaRepository<Brv, String> {
    Optional<Brv> findById(String id);
}
