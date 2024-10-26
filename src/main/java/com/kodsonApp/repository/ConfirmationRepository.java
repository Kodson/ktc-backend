package com.kodsonApp.repository;

import com.kodsonApp.domain.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {
    Confirmation findByToken(String token);

    void deleteByUserId(Long id);
}
