package com.kodsonApp.resource;

import com.kodsonApp.domain.FuelDischarge;
import com.kodsonApp.service.FuelDischargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/FuelDischarge")
public class FuelDischargeResource {

    @Autowired
    private FuelDischargeService fuelDischargeService;

    // Create or update a fuel discharge record
    @PostMapping
    public ResponseEntity<FuelDischarge> saveFuelDischarge(@RequestBody FuelDischarge fuelDischarge) {
        FuelDischarge savedFuel = fuelDischargeService.saveFuelDischarge(fuelDischarge);
        return ResponseEntity.ok(savedFuel);
    }

    // Retrieve all fuel discharge records
    @GetMapping
    public ResponseEntity<List<FuelDischarge>> getAllFuelDischarges() {
        List<FuelDischarge> discharges = fuelDischargeService.getAllFuelDischarges();
        return ResponseEntity.ok(discharges);
    }

    // Retrieve a single fuel discharge record by ID
    @GetMapping("/{id}")
    public ResponseEntity<FuelDischarge> getFuelDischargeById(@PathVariable String id) {
        return fuelDischargeService.getFuelDischargeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete a fuel discharge record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFuelDischarge(@PathVariable String id) {
        fuelDischargeService.deleteFuelDischargeById(id);
        return ResponseEntity.noContent().build();
    }
}

