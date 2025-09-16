package com.kodsonApp.service;

import com.kodsonApp.domain.Supply;
import com.kodsonApp.DTO.SupplyConfirmationRequest;
import com.kodsonApp.enumuration.SupplyStatus;
import com.kodsonApp.repository.SupplyRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SupplyService extends BaseService<Supply, String> {

    private final SupplyRepo supplyRepo;
    private final TankService tankService;
    private final ObjectMapper objectMapper;

    public SupplyService(SupplyRepo supplyRepo, TankService tankService, ObjectMapper objectMapper) {
        this.supplyRepo = supplyRepo;
        this.tankService = tankService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected JpaRepository<Supply, String> getRepository() {
        return supplyRepo;
    }

    @Override
    protected String getCacheName() {
        return "supplies";
    }

    @Override
    protected Class<Supply> getEntityClass() {
        return Supply.class;
    }

    @Transactional
    @Cacheable(value = "supplies", key = "'page-' + #page + '-' + #size")
    public Page<Supply> getAllSupplies(int page, int size) {
        return supplyRepo.findAll(PageRequest.of(page, size));
    }

    @Cacheable(value = "supplies", key = "#id")
    public Supply getSupply(String id) {
        return supplyRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supply not found with id: " + id));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "supplies", key = "'pending-supplies'"),
        @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).PENDING"),
        @CacheEvict(value = "supplies", allEntries = true, condition = "#result != null")
    })
    public Supply createSupply(Supply supply) {
        log.info("Starting createSupply for single supply");
        log.debug("Supply before processing: {}", supply);

        supply.setStatus(SupplyStatus.PENDING);
        supply.setCreatedAt(LocalDateTime.now());

        log.info("Attempting to save single supply to database");
        Supply savedSupply = supplyRepo.save(supply);
        log.info("Successfully saved single supply with ID: {}", savedSupply.getId());

        return savedSupply;
    }

    @Transactional
    @CacheEvict(value = "supplies", allEntries = true)
    public void deleteSupply(String id) {
        if (!supplyRepo.existsById(id)) {
            throw new EntityNotFoundException("Supply not found with id: " + id);
        }
        supplyRepo.deleteById(id);
    }

    @Transactional
    @Cacheable(value = "supplies", key = "'by-station-' + #station + '-page-' + #page + '-' + #size")
    public Page<Supply> getSupplyByStation(String station, int page, int size) {
        return supplyRepo.findByStation(station, PageRequest.of(page, size));
    }

    @Transactional
    @Caching(evict = {
        @CacheEvict(value = "supplies", key = "'pending-supplies'"),
        @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).PENDING"),
        @CacheEvict(value = "supplies", allEntries = true)
    })
    public List<Supply> createSupplies(Object suppliesInput) {
        log.info("Starting createSupplies with input type: {}", suppliesInput.getClass().getSimpleName());

        List<Supply> supplies;

        if (suppliesInput instanceof List<?>) {
            // Handle array of supplies - need to convert LinkedHashMaps to Supply objects
            @SuppressWarnings("unchecked")
            List<Object> rawList = (List<Object>) suppliesInput;
            log.info("Processing array of {} items", rawList.size());

            supplies = rawList.stream()
                    .map(item -> {
                        if (item instanceof LinkedHashMap) {
                            // Convert LinkedHashMap to Supply using ObjectMapper
                            Supply converted = objectMapper.convertValue(item, Supply.class);
                            log.debug("Converted LinkedHashMap to Supply: {}", converted);
                            return converted;
                        } else if (item instanceof Supply) {
                            log.debug("Already a Supply object: {}", item);
                            return (Supply) item;
                        } else {
                            throw new IllegalArgumentException("Invalid item type in supplies array: " + item.getClass().getSimpleName());
                        }
                    })
                    .collect(Collectors.toList());
        } else if (suppliesInput instanceof LinkedHashMap) {
            // Handle single supply object as LinkedHashMap - convert to Supply
            log.info("Processing single LinkedHashMap object");
            Supply supply = objectMapper.convertValue(suppliesInput, Supply.class);
            log.debug("Converted single LinkedHashMap to Supply: {}", supply);
            supplies = List.of(supply);
        } else if (suppliesInput instanceof Supply) {
            // Handle single supply object (already converted)
            log.info("Processing single Supply object");
            supplies = List.of((Supply) suppliesInput);
        } else {
            throw new IllegalArgumentException("Input must be either a single Supply object or a List of Supply objects, but got: " + suppliesInput.getClass().getSimpleName());
        }

        log.info("Setting status and timestamp for {} supplies", supplies.size());
        supplies.forEach(supply -> {
            // Validate required fields before saving
            log.debug("Validating supply: date={}, product={}, qty={}, station={}",
                supply.getDate(), supply.getProduct(), supply.getQty(), supply.getStation());

            if (supply.getDate() == null) {
                log.warn("Supply missing required date field");
            }
            if (supply.getProduct() == null || supply.getProduct().trim().isEmpty()) {
                log.warn("Supply missing required product field");
            }
            if (supply.getQty() == null) {
                log.warn("Supply missing required qty field");
            }
            if (supply.getStation() == null || supply.getStation().trim().isEmpty()) {
                log.warn("Supply missing required station field");
            }

            supply.setStatus(SupplyStatus.PENDING);
            supply.setCreatedAt(LocalDateTime.now());
            log.debug("Supply after processing: {}", supply);
        });

        log.info("Attempting to save {} supplies to database", supplies.size());

        // Save all supplies in one transaction
        List<Supply> savedSupplies = supplyRepo.saveAll(supplies);
        log.info("Successfully saved {} supplies to database", savedSupplies.size());

        // Log the IDs of saved supplies for verification
        savedSupplies.forEach(supply -> log.info("Saved supply with ID: {}", supply.getId()));

        return savedSupplies;
    }

    @Transactional
    @Caching(
        put = @CachePut(value = "supplies", key = "#id"),
        evict = {
            @CacheEvict(value = "supplies", key = "'pending-supplies'"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).PENDING"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).APPROVED"),
            @CacheEvict(value = "supplies", allEntries = true, condition = "#result != null")
        }
    )
    public Supply approveSupply(String id, String approvedBy, String reason) {
        Supply supply = getSupply(id);

        if (supply.getStatus() != SupplyStatus.PENDING) {
            throw new IllegalStateException("Supply can only be approved if it's in PENDING status");
        }

        supply.setStatus(SupplyStatus.APPROVED);
        supply.setApprovedBy(approvedBy);
        supply.setApprovedAt(LocalDateTime.now());
        supply.setApprovalReason(reason);

        log.info("Supply {} approved by {}", id, approvedBy);
        return supplyRepo.save(supply);
    }

    @Transactional
    @Caching(
        put = @CachePut(value = "supplies", key = "#id"),
        evict = {
            @CacheEvict(value = "supplies", key = "'pending-supplies'"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).PENDING"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).REJECTED"),
            @CacheEvict(value = "supplies", allEntries = true, condition = "#result != null")
        }
    )
    public Supply rejectSupply(String id, String rejectedBy, String reason) {
        Supply supply = getSupply(id);

        if (supply.getStatus() != SupplyStatus.PENDING) {
            throw new IllegalStateException("Supply can only be rejected if it's in PENDING status");
        }

        supply.setStatus(SupplyStatus.REJECTED);
        supply.setRejectedBy(rejectedBy);
        supply.setRejectedAt(LocalDateTime.now());
        supply.setRejectionReason(reason);

        log.info("Supply {} rejected by {}", id, rejectedBy);
        return supplyRepo.save(supply);
    }

    @Transactional
    @Caching(
        put = @CachePut(value = "supplies", key = "#request.id"),
        evict = {
            @CacheEvict(value = "supplies", key = "'pending-supplies'"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).APPROVED"),
            @CacheEvict(value = "supplies", key = "'by-status-' + T(com.kodsonApp.enumuration.SupplyStatus).RECEIVED"),
            @CacheEvict(value = "supplies", allEntries = true, condition = "#result != null")
        }
    )
    public Supply confirmSupplyReceipt(SupplyConfirmationRequest request) {
        Supply supply = getSupply(request.getId());

        if (supply.getStatus() != SupplyStatus.APPROVED) {
            throw new IllegalStateException("Supply must be approved before confirming receipt");
        }

        // Update supply with confirmation details
        supply.setStatus(SupplyStatus.RECEIVED);
        supply.setConfirmedBy(request.getConfirmedBy());
        supply.setConfirmedAt(request.getConfirmedAt());
        supply.setQtyReceived(request.getQtyR());
        supply.setOverage(request.getOverage());
        supply.setShortage(request.getShortage());
        supply.setReceiptNotes(request.getNotes());

        // Update tank stock based on the received quantity
        updateTankStockFromSupply(supply, request.getQtyR());

        log.info("Supply receipt confirmed for ID: {} by {}", request.getId(), request.getConfirmedBy());
        return supplyRepo.save(supply);
    }

    private void updateTankStockFromSupply(Supply supply, Double receivedQuantity) {
        try {
            tankService.updateTankStock(
                supply.getStation(),
                supply.getProduct(),
                receivedQuantity,
                supply.getConfirmedBy()
            );
            log.info("Tank stock updated for station: {}, product: {}, quantity: {}",
                    supply.getStation(), supply.getProduct(), receivedQuantity);
        } catch (Exception e) {
            log.error("Failed to update tank stock for supply {}: {}", supply.getId(), e.getMessage());
            // Don't fail the entire transaction, just log the error
        }
    }

    @Transactional
    public List<Supply> getSuppliesByStatus(SupplyStatus status) {
        return supplyRepo.findByStatus(status);
    }

    @Transactional
    @Cacheable(value = "supplies", key = "'pending-supplies'")
    public List<Supply> getPendingSupplies() {
        return supplyRepo.findByStatus(SupplyStatus.PENDING);
    }
    @Transactional
    public Supply getSupplyByStationDateAndProduct(String station, String date, String product) {
        log.info("Querying supply for station: {}, product: {}, date: {}", station, product, date);
        try {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startOfDay = sdf.parse(date);
            // Calculate end of day
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(startOfDay);
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            cal.set(java.util.Calendar.SECOND, 59);
            cal.set(java.util.Calendar.MILLISECOND, 999);
            java.util.Date endOfDay = cal.getTime();

            List<Supply> supplies = supplyRepo.findByStationAndProductAndDateBetween(station, product, startOfDay, endOfDay);
            log.info("Found {} supplies for station: {}, product: {}, date: {}", supplies.size(), station, product, date);
            if (supplies.isEmpty()) {
                log.warn("No supply found for station: {}, product: {}, date: {}", station, product, date);
                throw new EntityNotFoundException("No supply found for station: " + station + ", date: " + date + ", product: " + product);
            }
            // Return the first supply found for the day
            return supplies.get(0);
        } catch (java.text.ParseException e) {
            log.error("Invalid date format for supply query: {}. Expected yyyy-MM-dd", date);
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd.");
        }
    }
}
