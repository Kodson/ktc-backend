package com.kodsonApp.service;

import com.kodsonApp.domain.Tank;
import com.kodsonApp.domain.TankHistory;
import com.kodsonApp.enumuration.TankOperation;
import com.kodsonApp.repository.TankRepo;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TankService extends BaseService<Tank, String> {

    private final TankRepo tankRepo;
    private final TankHistoryService tankHistoryService;

    public TankService(TankRepo tankRepo, TankHistoryService tankHistoryService) {
        this.tankRepo = tankRepo;
        this.tankHistoryService = tankHistoryService;
    }

    @Override
    protected JpaRepository<Tank, String> getRepository() {
        return tankRepo;
    }

    @Override
    protected String getCacheName() {
        return "tanks";
    }

    @Override
    protected Class<Tank> getEntityClass() {
        return Tank.class;
    }

    @Cacheable(value = "tanks", key = "'all-tanks'")
    public List<Tank> getAllTanks() {
        return tankRepo.findAll();
    }

    @Cacheable(value = "tanks", key = "#id")
    public Optional<Tank> getTankById(String id) {
        return tankRepo.findById(id);
    }

    @Cacheable(value = "tanks", key = "'by-station-fuel-' + #station + '-' + #fuelType")
    public Optional<Tank> getTankByStationAndFuelType(String station, String fuelType) {
        return tankRepo.findByStationAndFuelType(station, fuelType);
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public Tank createTank(Tank tank) {
        tank.setCreatedAt(LocalDateTime.now());
        tank.setUpdatedAt(LocalDateTime.now());

        Tank savedTank = tankRepo.save(tank);

        // Record tank creation in history using SUPPLY instead of SUPPLY_RECEIVED
        // to avoid database constraint violation
        tankHistoryService.recordTankOperation(
            savedTank,
            TankOperation.SUPPLY,
            savedTank.getCurrentStock(),
            "System - Tank Created"
        );

        return savedTank;
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public Tank updateTank(String id, Tank tankDetails) {
        Tank tank = tankRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tank not found with id: " + id));

        Double previousStock = tank.getCurrentStock();

        tank.setName(tankDetails.getName());
        tank.setFuelType(tankDetails.getFuelType());
        tank.setCapacity(tankDetails.getCapacity());
        tank.setCurrentStock(tankDetails.getCurrentStock());
        tank.setStation(tankDetails.getStation());
        tank.setPricePerLiter(tankDetails.getPricePerLiter());
        tank.setMinLevel(tankDetails.getMinLevel());
        tank.setMaxLevel(tankDetails.getMaxLevel());
        tank.setUpdatedAt(LocalDateTime.now());

        Tank updatedTank = tankRepo.save(tank);

        // Record stock change if there was any
        if (!previousStock.equals(tankDetails.getCurrentStock())) {
            tankHistoryService.recordTankOperation(
                updatedTank,
                TankOperation.STOCK_ADJUSTMENT,
                tankDetails.getCurrentStock() - previousStock,
                "Manual Update"
            );
        }

        return updatedTank;
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public Tank updateTankStock(String station, String fuelType, Double quantity, String performedBy) {
        Tank tank = tankRepo.findByStationAndFuelType(station, fuelType)
                .orElseThrow(() -> new EntityNotFoundException(
                    "Tank not found for station: " + station + " and fuel type: " + fuelType));

        Double previousStock = tank.getCurrentStock();
        Double newStock = previousStock + quantity;

        if (newStock < 0) {
            throw new IllegalArgumentException("Cannot reduce stock below zero. Current: " + previousStock + ", Requested reduction: " + Math.abs(quantity));
        }

        if (newStock > tank.getCapacity()) {
            throw new IllegalArgumentException("Cannot exceed tank capacity. Capacity: " + tank.getCapacity() + ", Requested new stock: " + newStock);
        }

        tank.setCurrentStock(newStock);
        tank.setUpdatedAt(LocalDateTime.now());

        Tank updatedTank = tankRepo.save(tank);

        // Record the stock change in history
        TankOperation operation = quantity > 0 ? TankOperation.SUPPLY_RECEIVED : TankOperation.DISPENSED;
        tankHistoryService.recordTankOperation(updatedTank, operation, Math.abs(quantity), performedBy);

        return updatedTank;
    }

    @Transactional
    @CacheEvict(value = "tanks", key = "#id")
    public void deleteTank(String id) {
        if (!tankRepo.existsById(id)) {
            throw new EntityNotFoundException("Tank not found with id: " + id);
        }
        tankRepo.deleteById(id);
    }

    @Cacheable(value = "tanks", key = "'history-' + #id")
    public List<TankHistory> getTankHistory(String id) {
        // Verify tank exists
        tankRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tank not found with id: " + id));

        return tankHistoryService.getTankHistory(id);
    }

    @Cacheable(value = "tanks", key = "'low-stock'")
    public List<Tank> getLowStockTanks() {
        return tankRepo.findAll().stream()
                .filter(tank -> tank.getMinLevel() != null && tank.getCurrentStock() <= tank.getMinLevel())
                .toList();
    }

    @Cacheable(value = "tanks", key = "'by-station-' + #station")
    public List<Tank> getTanksByStation(String station) {
        return tankRepo.findAll().stream()
                .filter(tank -> tank.getStation().equals(station))
                .toList();
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public void updateTankPrice(String tankId, Double newPrice, String updatedBy) {
        Tank tank = tankRepo.findById(tankId)
                .orElseThrow(() -> new EntityNotFoundException("Tank not found with id: " + tankId));

        Double oldPrice = tank.getPricePerLiter();
        tank.setPricePerLiter(newPrice);
        tank.setUpdatedAt(LocalDateTime.now());

        tankRepo.save(tank);

        // Record price change in history using MAINTENANCE since it's a system operation
        tankHistoryService.recordTankOperation(
            tank,
            TankOperation.MAINTENANCE,
            newPrice,
            updatedBy + " - Price Update"
        );
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public Tank manageTank(String id, Tank tankDetails) {
        if (id == null || id.trim().isEmpty()) {
            // Create new tank
            return createTank(tankDetails);
        } else {
            // Update existing tank
            return updateTank(id, tankDetails);
        }
    }

    @Transactional
    @CacheEvict(value = "tanks", allEntries = true)
    public void updateTankPrices(String priceUpdateId, List<String> tankIds, double newPrice) {
        for (String tankId : tankIds) {
            Tank tank = tankRepo.findById(tankId)
                    .orElseThrow(() -> new EntityNotFoundException("Tank not found with id: " + tankId));

            tank.setPricePerLiter(newPrice);
            tank.setUpdatedAt(LocalDateTime.now());

            tankRepo.save(tank);

            // Record price change in history using MAINTENANCE since it's a system operation
            tankHistoryService.recordTankOperation(
                tank,
                TankOperation.MAINTENANCE,
                newPrice,
                "Price Update #" + priceUpdateId
            );
        }
    }
}
