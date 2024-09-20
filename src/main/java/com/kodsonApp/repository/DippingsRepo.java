package com.kodsonApp.repository;

import com.kodsonApp.domain.Dippings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DippingsRepo extends JpaRepository<Dippings, String> {
}
