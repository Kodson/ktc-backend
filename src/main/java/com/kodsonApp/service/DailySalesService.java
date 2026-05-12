package com.kodsonApp.service;

import com.kodsonApp.DTO.ApprovalRequest;
import com.kodsonApp.domain.DailySales;
import com.kodsonApp.DTO.ValidationRequest;
import com.kodsonApp.DTO.DailySalesWithCashToBankResponse;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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
    @CacheEvict(value = {"dailySales", "monthlySales"}, allEntries = true)
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
    @CacheEvict(value = {"dailySales", "monthlySales"}, allEntries = true)
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
    @CacheEvict(value = {"dailySales", "monthlySales"}, allEntries = true)
    public void deleteDailySales(String id) {
        if (!dailySalesRepo.existsById(id)) {
            throw new EntityNotFoundException("DailySales not found with id: " + id);
        }
        dailySalesRepo.deleteById(id);
        dailySalesRepo.flush(); // Force immediate database deletion
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

    @Cacheable(value = "dailySales", key = "'latest-by-date-' + #station + '-' + #product + '-' + #targetDate.toString()")
    public DailySalesWithCashToBankResponse getLatestDailySalesByStationAndProductByDate(String station, String product, LocalDateTime targetDate) {
        log.info("Searching for nearest sales for station: {}, product: {} up to target date: {}", station, product, targetDate);
        
        // Find records by business date, not creation timestamp
        String targetDateStr = targetDate.toLocalDate().toString(); // Convert to yyyy-MM-dd format
        List<DailySales> allRecords = dailySalesRepo.findByStationAndProductOrderByDateDescCreatedAtDesc(station, product);
        
        // First, try to find exact match for target date
        Optional<DailySales> exactMatch = allRecords.stream()
            .filter(record -> {
                if (record.getDate() == null || record.getDate().trim().isEmpty()) {
                    return false;
                }
                try {
                    // Check if business date matches target date exactly
                    return record.getDate().equals(targetDateStr);
                } catch (Exception e) {
                    log.warn("Invalid date format in record: {}, date: {}", record.getId(), record.getDate());
                    return false;
                }
            })
            .findFirst(); // Get the latest record for the exact target date
        
        Optional<DailySales> latestSales;
        if (exactMatch.isPresent()) {
            // Use exact match if found
            latestSales = exactMatch;
            log.info("Found exact match for target date: {}", targetDateStr);
        } else {
            // Fall back to nearest previous date
            latestSales = allRecords.stream()
                .filter(record -> {
                    if (record.getDate() == null || record.getDate().trim().isEmpty()) {
                        return false;
                    }
                    try {
                        // Compare business dates (yyyy-MM-dd format) - find dates before target
                        return record.getDate().compareTo(targetDateStr) < 0;
                    } catch (Exception e) {
                        log.warn("Invalid date format in record: {}, date: {}", record.getId(), record.getDate());
                        return false;
                    }
                })
                .findFirst(); // Already sorted by date desc, created_at desc
            log.info("No exact match found, searching for nearest previous date before: {}", targetDateStr);
        }
        
        if (latestSales.isEmpty()) {
            log.warn("No previous daily sales found for station: {}, product: {} up to business date: {}", station, product, targetDateStr);
            throw new EntityNotFoundException("No previous daily sales found for station: " + station + ", product: " + product + " up to business date: " + targetDateStr);
        }
        
        DailySales mainRecord = latestSales.get();
        boolean isExactMatch = mainRecord.getDate().equals(targetDateStr);
        log.info("Found {} main record - ID: {}, Business Date: {}, Created: {}, Product: {} (target date: {})", 
                 isExactMatch ? "exact match" : "nearest previous", 
                 mainRecord.getId(), mainRecord.getDate(), mainRecord.getCreatedAt(), mainRecord.getProduct(), targetDateStr);
        
        // Find PMS and AGO records for the EXACT target business date
        List<String> productsToCheck = Arrays.asList("PMS", "AGO");
        
        // Build product data map by finding records for the exact target business date
        Map<String, DailySalesWithCashToBankResponse.ProductCashToBankData> productDataMap = new HashMap<>();
        for (String productToCheck : productsToCheck) {
            // Find records for the exact target business date
            List<DailySales> allProductRecords = dailySalesRepo.findByStationAndProductOrderByDateDescCreatedAtDesc(station, productToCheck);
            
            List<DailySales> targetDateRecords = allProductRecords.stream()
                .filter(record -> {
                    if (record.getDate() == null || record.getDate().trim().isEmpty()) {
                        return false;
                    }
                    try {
                        // Check if business date matches target date exactly
                        return record.getDate().equals(targetDateStr);
                    } catch (Exception e) {
                        log.warn("Invalid date format in record: {}, date: {}", record.getId(), record.getDate());
                        return false;
                    }
                })
                .collect(Collectors.toList());
            
            if (!targetDateRecords.isEmpty()) {
                // Use the latest record from the target date (first in list since sorted by date desc, created desc)
                DailySales record = targetDateRecords.get(0);
                DailySalesWithCashToBankResponse.ProductCashToBankData productData = 
                    DailySalesWithCashToBankResponse.ProductCashToBankData.builder()
                        .productName(record.getProduct())
                        .cashToBank(record.getCashToBank())
                        .salesL(record.getSalesL())
                        .value(record.getValue())
                        .openSL(record.getOpenSL())
                        .closingSL(record.getClosingSL())
                        .actualCash(record.getActualCash())
                        .recordDate(record.getCreatedAt())
                        .recordId(record.getId())
                        .build();
                productDataMap.put(record.getProduct(), productData);
                log.info("Found {} record for exact target business date - ID: {}, Business Date: {}, Created: {}, CashToBank: {}", 
                         productToCheck, record.getId(), record.getDate(), record.getCreatedAt(), record.getCashToBank());
            } else {
                log.warn("No {} record found for station: {} on exact target business date: {}", productToCheck, station, targetDateStr);
            }
        }
        
        log.info("Built response - Main record business date: {}, Target business date: {}, Product data count for exact date: {}", 
                mainRecord.getDate(), targetDateStr, productDataMap.size());
        
        DailySalesWithCashToBankResponse response = DailySalesWithCashToBankResponse.builder()
            .id(mainRecord.getId())
            .station(mainRecord.getStation())
            .date(mainRecord.getDate())
            .product(mainRecord.getProduct())
            // Sales volume and inventory data
            .openSL(mainRecord.getOpenSL())
            .supply(mainRecord.getSupply())
            .overageShortageL(mainRecord.getOverageShortageL())
            .availableL(mainRecord.getAvailableL())
            .closingSL(mainRecord.getClosingSL())
            .differenceL(mainRecord.getDifferenceL())
            .checkL(mainRecord.getCheckL())
            .openSR(mainRecord.getOpenSR())
            .closingSR(mainRecord.getClosingSR())
            .returnTT(mainRecord.getReturnTT())
            .salesL(mainRecord.getSalesL())
            // Financial data
            .rate(mainRecord.getRate())
            .value(mainRecord.getValue())
            .cashSales(mainRecord.getCashSales())
            .creditSales(mainRecord.getCreditSales())
            .advances(mainRecord.getAdvances())
            .shortageMomo(mainRecord.getShortageMomo())
            .cashAvailable(mainRecord.getCashAvailable())
            .repaymentShortageMomo(mainRecord.getRepaymentShortageMomo())
            .actualCash(mainRecord.getActualCash())
            .bankLodgement(mainRecord.getBankLodgement())
            .cashToBank(mainRecord.getCashToBank())
            .receivedFromDebtors(mainRecord.getReceivedFromDebtors())
            .repaymentAdvances(mainRecord.getRepaymentAdvances())
            // Metadata
            .enteredBy(mainRecord.getEnteredBy())
            .createdAt(mainRecord.getCreatedAt())
            .targetDateTime(targetDate)
            .productData(productDataMap)
            .build();
        
        log.info("Response for getLatestDailySalesByStationAndProductByDate - Station: {}, Product: {}, Target Date: {}, " +
                 "Main Record ID: {}, Main Record Date: {}, Main CashToBank: {}, Sales L: {}, Value: {}, " +
                 "Product Data Count: {}, Available Products: {}", 
                 station, product, targetDate, response.getId(), response.getCreatedAt(), 
                 response.getCashToBank(), response.getSalesL(), response.getValue(),
                 response.getProductData().size(), response.getProductData().keySet());
        
        // Log detailed productData content
        for (Map.Entry<String, DailySalesWithCashToBankResponse.ProductCashToBankData> entry : response.getProductData().entrySet()) {
            DailySalesWithCashToBankResponse.ProductCashToBankData productData = entry.getValue();
            log.info("ProductData[{}] - ID: {}, Date: {}, CashToBank: {}, SalesL: {}, Value: {}, OpenSL: {}, ClosingSL: {}, ActualCash: {}", 
                     entry.getKey(), productData.getRecordId(), productData.getRecordDate(), 
                     productData.getCashToBank(), productData.getSalesL(), productData.getValue(),
                     productData.getOpenSL(), productData.getClosingSL(), productData.getActualCash());
        }
        
        return response;
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

    /**
     * Convenience method to get latest sales by target date using string date format
     * @param station Station name
     * @param product Product name
     * @param targetDateStr Target date in format "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss"
     * @return DailySalesWithCashToBankResponse containing sales data and cashToBank information
     */
    @Cacheable(value = "dailySales", key = "'latest-by-string-date-' + #station + '-' + #product + '-' + #targetDateStr")
    public DailySalesWithCashToBankResponse getLatestDailySalesByStationAndProductByDate(String station, String product, String targetDateStr) {
        LocalDateTime targetDate;
        try {
            // Try parsing as date-time first
            if (targetDateStr.contains(" ")) {
                targetDate = LocalDateTime.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                // Parse as date and set to end of day
                LocalDate date = LocalDate.parse(targetDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                targetDate = date.atTime(23, 59, 59);
            }
        } catch (Exception e) {
            log.error("Invalid date format: {}. Expected 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm:ss'", targetDateStr);
            throw new IllegalArgumentException("Invalid date format: " + targetDateStr + ". Expected 'yyyy-MM-dd' or 'yyyy-MM-dd HH:mm:ss'");
        }
        
        return getLatestDailySalesByStationAndProductByDate(station, product, targetDate);
    }

    /**
     * Convenience method to get latest sales as of a specific LocalDate (end of day)
     * @param station Station name
     * @param product Product name
     * @param targetDate Target date
     * @return DailySalesWithCashToBankResponse containing sales data and cashToBank information
     */
    @Cacheable(value = "dailySales", key = "'latest-by-local-date-' + #station + '-' + #product + '-' + #targetDate.toString()")
    public DailySalesWithCashToBankResponse getLatestDailySalesByStationAndProductByDate(String station, String product, LocalDate targetDate) {
        LocalDateTime targetDateTime = targetDate.atTime(23, 59, 59);
        return getLatestDailySalesByStationAndProductByDate(station, product, targetDateTime);
    }


}
