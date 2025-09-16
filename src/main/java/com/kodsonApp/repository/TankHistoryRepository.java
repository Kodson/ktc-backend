package com.kodsonApp.repository;

import com.kodsonApp.domain.TankHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TankHistoryRepository extends JpaRepository<TankHistory, String> {

    List<TankHistory> findByTankId(String tankId);

    List<TankHistory> findByTankIdOrderByPerformedAtDesc(String tankId);

    List<TankHistory> findByTankIdAndPerformedAtBetweenOrderByPerformedAtDesc(
            String tankId, LocalDateTime startDate, LocalDateTime endDate);
}
