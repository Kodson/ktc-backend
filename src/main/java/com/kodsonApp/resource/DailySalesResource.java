package com.kodsonApp.resource;

import com.kodsonApp.DTO.ApprovalRequest;
import com.kodsonApp.domain.DailySales;
import com.kodsonApp.DTO.ValidationRequest;
import com.kodsonApp.enumuration.ValidationStatus;
import com.kodsonApp.service.DailySalesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("/api/dailysales")
@CrossOrigin(origins = "*")
public class DailySalesResource {

    private final DailySalesService dailySalesService;

    public DailySalesResource(DailySalesService dailySalesService) {
        this.dailySalesService = dailySalesService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER')")
    public ResponseEntity<Page<DailySales>> getAllDailySales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(dailySalesService.getAllDailySales(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER', 'ATTENDANT')")
    public ResponseEntity<DailySales> getDailySales(@PathVariable String id) {
        return ResponseEntity.ok(dailySalesService.getDailySales(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER', 'ATTENDANT')")
    public ResponseEntity<DailySales> createDailySales(@Valid @RequestBody DailySales dailySales) {
        return ResponseEntity.ok(dailySalesService.createDailySales(dailySales));
    }

    @PutMapping("/{id}/validate")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER')")
    public ResponseEntity<DailySales> validateDailySales(@PathVariable String id,
                                                        @Valid @RequestBody ValidationRequest validationRequest) {
        DailySales validatedSales = dailySalesService.validateDailySales(id, validationRequest);
        return ResponseEntity.ok(validatedSales);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER')")
    public ResponseEntity<DailySales> approveDailySales(@PathVariable String id,
                                                        @Valid @RequestBody ApprovalRequest approvalRequest) {
        DailySales validatedSales = dailySalesService.approveDailySales(id, approvalRequest);
        return ResponseEntity.ok(validatedSales);
    }

    @GetMapping("/station/{station}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER', 'ATTENDANT')")
    public ResponseEntity<Page<DailySales>> getDailySalesByStation(
            @PathVariable String station,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(direction, sortBy));

        return ResponseEntity.ok(dailySalesService.getDailySalesByStation(station, pageable));
    }



    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER')")
    public ResponseEntity<List<DailySales>> getDailySalesByStatus(@PathVariable ValidationStatus status) {
        return ResponseEntity.ok(dailySalesService.getDailySalesByStatus(status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDailySales(@PathVariable String id) {
        dailySalesService.deleteDailySales(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/latest/{station}/{product}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER', 'ATTENDANT')")
    public ResponseEntity<DailySales> getLatestDailySalesByStationAndProduct(
            @PathVariable String station,
            @PathVariable String product) {
        DailySales latestSales = dailySalesService.getLatestDailySalesByStationAndProduct(station, product);
        return ResponseEntity.ok(latestSales);
    }

    @GetMapping("/history/{station}/{product}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'STATION_MANAGER', 'ATTENDANT')")
    public ResponseEntity<List<DailySales>> getDailySalesHistoryByStationAndProduct(
            @PathVariable String station,
            @PathVariable String product) {
        List<DailySales> salesHistory = dailySalesService.getDailySalesHistoryByStationAndProduct(station, product);
        return ResponseEntity.ok(salesHistory);
    }

}
