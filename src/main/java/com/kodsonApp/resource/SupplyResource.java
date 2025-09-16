package com.kodsonApp.resource;

import com.kodsonApp.domain.Supply;
import com.kodsonApp.DTO.SupplyConfirmationRequest;
import com.kodsonApp.enumuration.SupplyStatus;
import com.kodsonApp.service.SupplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/supply")
@CrossOrigin(origins = "*")
public class SupplyResource {

    private final SupplyService supplyService;
    private final ObjectMapper objectMapper;

    public SupplyResource(SupplyService supplyService, ObjectMapper objectMapper) {
        this.supplyService = supplyService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<Page<Supply>> getAllSupplies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(supplyService.getAllSupplies(page, size));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<Supply> getSupply(@PathVariable String id) {
        return ResponseEntity.ok(supplyService.getSupply(id));
    }
    /*
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<Supply> createSupply(@Valid @RequestBody Supply supply) {
        return ResponseEntity.ok(supplyService.createSupply(supply));
    }
    */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<List<Supply>> createSupplies(@RequestBody Object suppliesInput) {
        List<Supply> result = supplyService.createSupplies(suppliesInput);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<Supply> approveSupply(@PathVariable String id, @RequestBody Map<String, String> request) {
        String approvedBy = request.get("approvedBy");
        String reason = request.get("reason");

        if (approvedBy == null || approvedBy.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(supplyService.approveSupply(id, approvedBy, reason));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<Supply> rejectSupply(@PathVariable String id, @RequestBody Map<String, String> request) {
        String rejectedBy = request.get("rejectedBy");
        String reason = request.get("reason");

        if (rejectedBy == null || rejectedBy.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(supplyService.rejectSupply(id, rejectedBy, reason));
    }

    @PutMapping("/confirm/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER', 'ROLE_ATTENDANT')")
    public ResponseEntity<Supply> confirmSupplyReceipt(@PathVariable String id,
                                                      @Valid @RequestBody SupplyConfirmationRequest request) {
        // Ensure the ID in the request matches the path variable
        request.setId(id);
        return ResponseEntity.ok(supplyService.confirmSupplyReceipt(request));
    }

    @GetMapping("/station/{station}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER', 'ROLE_ATTENDANT')")
    public ResponseEntity<Page<Supply>> getSuppliesByStation(
            @PathVariable String station,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(supplyService.getSupplyByStation(station, page, size));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<List<Supply>> getSuppliesByStatus(@PathVariable SupplyStatus status) {
        return ResponseEntity.ok(supplyService.getSuppliesByStatus(status));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<List<Supply>> getPendingSupplies() {
        return ResponseEntity.ok(supplyService.getPendingSupplies());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteSupply(@PathVariable String id) {
        supplyService.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/supply-data/{station}/{date}/{product}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN', 'ROLE_MANAGER', 'ROLE_STATION_MANAGER')")
    public ResponseEntity<?> getSupplyByStationDateAndProduct(
            @PathVariable("station") String station,
            @PathVariable String date,
            @PathVariable String product) {
        try {
            Supply supply = supplyService.getSupplyByStationDateAndProduct(station, date, product);
            return ResponseEntity.ok(supply);
        } catch (jakarta.persistence.EntityNotFoundException ex) {
            return ResponseEntity.status(404).body(java.util.Map.of("message", ex.getMessage()));
        } catch (java.time.format.DateTimeParseException ex) {
            return ResponseEntity.badRequest().body(java.util.Map.of("message", "Invalid date format. Expected yyyy-MM-dd."));
        }
    }
}
