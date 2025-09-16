package com.kodsonApp.service;

import com.kodsonApp.domain.Tank;
import com.kodsonApp.domain.TankHistory;
import com.kodsonApp.enumuration.TankOperation;
import com.kodsonApp.repository.TankHistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class TankHistoryService extends BaseService<TankHistory, String> {

    private final TankHistoryRepository tankHistoryRepository;

    public TankHistoryService(TankHistoryRepository tankHistoryRepository) {
        this.tankHistoryRepository = tankHistoryRepository;
    }

    @Override
    protected JpaRepository<TankHistory, String> getRepository() {
        return tankHistoryRepository;
    }

    @Override
    protected String getCacheName() {
        return "tankHistory";
    }

    @Override
    protected Class<TankHistory> getEntityClass() {
        return TankHistory.class;
    }

    public void recordTankOperation(Tank tank, TankOperation operation, Double quantity, String performedBy) {
        TankHistory history = new TankHistory();
        history.setTankId(tank.getId());
        history.setOperation(operation);
        history.setQuantity(quantity);
        history.setPerformedBy(performedBy);
        history.setPerformedAt(LocalDateTime.now());
        history.setPreviousLevel(tank.getCurrentStock());

        // Calculate new level based on operation
        Double newLevel = calculateNewLevel(tank.getCurrentStock(), quantity, operation);
        history.setNewLevel(newLevel);

        tankHistoryRepository.save(history);
    }

    private Double calculateNewLevel(Double currentLevel, Double quantity, TankOperation operation) {
        if (operation == null || quantity == null) {
            return currentLevel;
        }

        // Use string comparison to avoid enum dependency issues
        String operationName = operation.name();

        switch (operationName) {
            case "SUPPLY_RECEIVED":
            case "FUEL_ADDED":
            case "SUPPLY":
                return currentLevel + quantity;
            case "DISPENSED":
            case "FUEL_REMOVED":
            case "SALE":
                return currentLevel - quantity;
            case "STOCK_ADJUSTMENT":
            case "ADJUSTMENT":
                return quantity; // Direct adjustment to specific level
            case "DIPPING":
            case "CALIBRATION":
            case "MAINTENANCE":
            case "TANK_CREATED":
            case "PRICE_UPDATE":
            default:
                return currentLevel; // No quantity change for these operations
        }
    }

    public List<TankHistory> getTankHistory(String tankId) {
        return tankHistoryRepository.findByTankIdOrderByPerformedAtDesc(tankId);
    }

    public List<TankHistory> getTankHistoryByDateRange(String tankId, LocalDateTime startDate, LocalDateTime endDate) {
        return tankHistoryRepository.findByTankIdAndPerformedAtBetweenOrderByPerformedAtDesc(tankId, startDate, endDate);
    }
}
