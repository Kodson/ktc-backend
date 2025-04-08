package com.kodsonApp.repository;

import com.kodsonApp.domain.BdcAgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BdcAgoRepo extends JpaRepository<BdcAgo, String> {
    Optional<BdcAgo> findById(String id);
}
