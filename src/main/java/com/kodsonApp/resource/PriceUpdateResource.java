package com.kodsonApp.resource;

import com.kodsonApp.DTO.ApprovalRequest;
import com.kodsonApp.domain.PriceUpdate;
import com.kodsonApp.service.PriceUpdateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.*;

@RestController
@RequestMapping("/api/price-updates")
@CrossOrigin(origins = "*")
public class PriceUpdateResource {

    private final PriceUpdateService service;

    public PriceUpdateResource(PriceUpdateService service) {
        this.service = service;
    }

    // Save a new price update
    @PostMapping
    public ResponseEntity<PriceUpdate> createUpdate(@RequestBody PriceUpdate update) {
        PriceUpdate saved = service.savePriceUpdate(update);
        return ResponseEntity.ok(saved);
    }

    // Fetch all updates
    @GetMapping("/history")
    public ResponseEntity<List<PriceUpdate>> getAllUpdates() {
        return ResponseEntity.ok(service.getAllUpdates());
    }

    // Fetch update by ID
    @GetMapping("/{id}")
    public ResponseEntity<PriceUpdate> getUpdateById(@PathVariable UUID id) {
        PriceUpdate update = service.getUpdateById(id);
        if (update == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(update);
    }

    // 🔹 Fetch pending updates
    @GetMapping("/pending")
    public ResponseEntity<List<PriceUpdate>> getPendingUpdates() {
        return ResponseEntity.ok(service.getPendingUpdates());
    }

    // Approve single update endpoint
    @PutMapping("/approve/{id}")
    public ResponseEntity<PriceUpdate> approveUpdate(@PathVariable UUID id, @RequestBody ApprovalRequest request) {
        PriceUpdate update = service.approveUpdate(id, request.getApprovedBy());
        return ResponseEntity.ok(update);
    }

    // Reject single update endpoint
    @PutMapping("/{id}/reject")
    public ResponseEntity<PriceUpdate> rejectUpdate(@PathVariable UUID id, @RequestBody ApprovalRequest request) {
        PriceUpdate update = service.rejectUpdate(id, request.getApprovedBy(), request.getReason());
        return ResponseEntity.ok(update);
    }

    // 🔹 Bulk approve
    @PostMapping("/bulk-approve")
    public ResponseEntity<List<PriceUpdate>> bulkApprove(@RequestBody List<UUID> ids,
                                                         @RequestParam String approvedBy) {
        return ResponseEntity.ok(service.bulkApprove(ids, approvedBy));
    }

    // 🔹 Statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getStatistics() {
        return ResponseEntity.ok(service.fetchStatistics());
    }
}
