package com.kodsonApp.service;

import com.kodsonApp.DTO.ApprovalRequest;
import com.kodsonApp.domain.DailySales;
import com.kodsonApp.DTO.ValidationRequest;
import com.kodsonApp.enumuration.ValidationStatus;
import com.kodsonApp.repository.DailySalesRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class DailySalesService extends BaseService<DailySales, String> {

    @Autowired
    private final DailySalesRepo dailySalesRepo;

    @Override
    protected JpaRepository<DailySales, String> getRepository() {
        return dailySalesRepo;
    }

    @Override
    protected String getCacheName() {
        return "dailySales";
    }

    @Override
    protected Class<DailySales> getEntityClass() {
        return DailySales.class;
    }

    @Cacheable(value = "dailySales", key = "'page-' + #page + '-' + #size")
    public Page<DailySales> getAllDailySales(int page, int size) {
        return dailySalesRepo.findAll(PageRequest.of(page, size));
    }

    @Cacheable(value = "dailySales", key = "#id")
    public DailySales getDailySales(String id) {
        return dailySalesRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("DailySales not found with id: " + id));
    }

    @Transactional
    @CacheEvict(value = "dailySales", allEntries = true)
    public DailySales createDailySales(DailySales dailySales) {
        dailySales.setCreatedAt(LocalDateTime.now());
        dailySales.setUpdatedAt(LocalDateTime.now());
        if (dailySales.getStatus() == null) {
            dailySales.setStatus(ValidationStatus.PENDING);
        }

        log.info("Creating new daily sales record for station: {}", dailySales.getStation());
        return dailySalesRepo.save(dailySales);
    }

    @Transactional
    @CacheEvict(value = "dailySales", allEntries = true)
    public DailySales validateDailySales(String id, ValidationRequest validationRequest) {
        DailySales dailySales = getDailySales(id);

        dailySales.setStatus(validationRequest.getStatus());
        dailySales.setValidatedBy(validationRequest.getValidatedBy());
        dailySales.setValidatedAt(LocalDateTime.now());
        dailySales.setValidationNotes(validationRequest.getNotes());
        dailySales.setUpdatedAt(LocalDateTime.now());

        log.info("Daily sales {} validated with status: {} by {}", id, validationRequest.getStatus(), validationRequest.getValidatedBy());
        return dailySalesRepo.save(dailySales);
    }

    @Transactional
    @CacheEvict(value = "dailySales", allEntries = true)
    public DailySales approveDailySales(String id, ApprovalRequest approvalRequest) {
        DailySales dailySales = getDailySales(id);

        dailySales.setStatus(approvalRequest.getStatus());
        dailySales.setValidatedBy(approvalRequest.getApprovedBy());
        dailySales.setValidatedAt(LocalDateTime.now());
        dailySales.setValidationNotes(approvalRequest.getNotes());
        dailySales.setUpdatedAt(LocalDateTime.now());

        log.info("Daily sales {} validated with status: {} by {}", id, approvalRequest.getStatus(), approvalRequest.getApprovedBy());
        return dailySalesRepo.save(dailySales);
    }

    @Transactional
    public Page<DailySales> getDailySalesByStation(String station, Pageable pageable) {
        return dailySalesRepo.findByStation(station, pageable);
    }

    @Cacheable(value = "dailySales", key = "'by-status-' + #status")
    public List<DailySales> getDailySalesByStatus(ValidationStatus status) {
        return dailySalesRepo.findByStatus(status);
    }

    @Transactional
    @CacheEvict(value = "dailySales", key = "#id")
    public void deleteDailySales(String id) {
        if (!dailySalesRepo.existsById(id)) {
            throw new EntityNotFoundException("DailySales not found with id: " + id);
        }
        dailySalesRepo.deleteById(id);
        log.info("Deleted daily sales record with id: {}", id);
    }

    @Cacheable(value = "dailySales", key = "'pending-sales'")
    public List<DailySales> getPendingDailySales() {
        return dailySalesRepo.findByStatus(ValidationStatus.PENDING);
    }

    @Cacheable(value = "dailySales", key = "'validated-sales'")
    public List<DailySales> getValidatedDailySales() {
        return dailySalesRepo.findByStatus(ValidationStatus.VALIDATED);
    }

    @Cacheable(value = "dailySales", key = "'latest-' + #station + '-' + #product")
    public DailySales getLatestDailySalesByStationAndProduct(String station, String product) {
        log.info("Searching for latest daily sales for station: {} and product: {}", station, product);
        return dailySalesRepo.findFirstByStationAndProductOrderByCreatedAtDesc(station, product)
                .orElseThrow(() -> {
                    log.warn("No daily sales found for station: {} and product: {}", station, product);
                    return new EntityNotFoundException("No daily sales found for station: " + station + " and product: " + product);
                });
    }

    @Cacheable(value = "dailySales", key = "'latest-optional-' + #station + '-' + #product")
    @Transactional
    public Optional<DailySales> getLatestDailySalesByStationAndProductOptional(String station, String product) {
        log.info("Searching for latest daily sales for station: {} and product: {}", station, product);
        Optional<DailySales> result = dailySalesRepo.findFirstByStationAndProductOrderByCreatedAtDesc(station, product);
        if (result.isEmpty()) {
            log.warn("No daily sales found for station: {} and product: {}", station, product);
        }
        return result;
    }

    @Cacheable(value = "dailySales", key = "'history-' + #station + '-' + #product")
    public List<DailySales> getDailySalesHistoryByStationAndProduct(String station, String product) {
        return dailySalesRepo.findByStationAndProductOrderByCreatedAtDesc(station, product);
    }


}
