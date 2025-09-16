package com.kodsonApp.resource;

import com.kodsonApp.domain.Tank;
import com.kodsonApp.domain.TankHistory;
import com.kodsonApp.service.TankHistoryService;
import com.kodsonApp.service.TankService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/tanks")
@CrossOrigin(origins = "*")
public class TankResource {

    private final TankHistoryService tankHistoryService;
    private final TankService tankService;

    public TankResource(TankHistoryService tankHistoryService, TankService tankService) {
        this.tankHistoryService = tankHistoryService;
        this.tankService = tankService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'ATTENDANT')")
    public ResponseEntity<List<Tank>> getAllTanks() {
        return ResponseEntity.ok(tankService.getAllTanks());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'ATTENDANT')")
    public ResponseEntity<Tank> getTankById(@PathVariable String id) {
        return tankService.getTankById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<Tank> createTank(@Valid @RequestBody Tank tank) {
        return ResponseEntity.ok(tankService.createTank(tank));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<Tank> updateTank(@PathVariable String id, @Valid @RequestBody Tank updatedTank) {
        return ResponseEntity.ok(tankService.manageTank(id, updatedTank));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteTank(@PathVariable String id) {
        tankService.deleteTank(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER')")
    public ResponseEntity<List<TankHistory>> getTankHistory(@PathVariable String id) {
        List<TankHistory> history = tankService.getTankHistory(id);
        if (history == null || history.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'MANAGER', 'ATTENDANT')")
    public ResponseEntity<Tank> updateTankStock(@PathVariable String id,
                                              @RequestParam String station,
                                              @RequestParam String fuelType,
                                              @RequestParam Double quantity,
                                              @RequestParam String performedBy) {
        return ResponseEntity.ok(tankService.updateTankStock(station, fuelType, quantity, performedBy));
    }
}
