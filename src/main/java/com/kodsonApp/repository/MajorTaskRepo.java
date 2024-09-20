package com.kodsonApp.repository;

import com.kodsonApp.domain.MajorTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface MajorTaskRepo extends JpaRepository<MajorTask, String> {
    Optional<MajorTask> findById(String id);
}
