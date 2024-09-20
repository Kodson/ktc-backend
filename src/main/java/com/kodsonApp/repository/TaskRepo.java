package com.kodsonApp.repository;

import com.kodsonApp.domain.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TaskRepo extends JpaRepository<Tasks, String> {
    @Override
    Optional<Tasks> findById(String id);
}
