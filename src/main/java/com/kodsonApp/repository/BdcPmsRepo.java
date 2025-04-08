package com.kodsonApp.repository;


import com.kodsonApp.domain.BdcPms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface BdcPmsRepo extends JpaRepository<BdcPms, String> {
    Optional<BdcPms> findById(String id);
}
