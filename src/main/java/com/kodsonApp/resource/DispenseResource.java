package com.kodsonApp.resource;

import com.kodsonApp.domain.Dispense;
import com.kodsonApp.service.DispenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api2/FuelDispense")
public class DispenseResource {

    @Autowired
    private DispenseService dispenseService;

    // Get all Dispense records
    @GetMapping
    public List<Dispense> getAllDispenses() {
        return dispenseService.getAllDispenses();
    }

    // Get a specific Dispense record by ID
    @GetMapping("/{id}")
    public ResponseEntity<Dispense> getDispenseById(@PathVariable String id) {
        Optional<Dispense> dispense = dispenseService.getDispenseById(id);
        return dispense.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new Dispense record
    @PostMapping
    public Dispense createDispense(@RequestBody Dispense dispense) {
        return dispenseService.saveDispense(dispense);
    }

    // Update an existing Dispense record
    @PutMapping("/{id}")
    public ResponseEntity<Dispense> updateDispense(@PathVariable String id, @RequestBody Dispense dispenseDetails) {
        Dispense updatedDispense = dispenseService.updateDispense(id, dispenseDetails);
        return ResponseEntity.ok(updatedDispense);
    }

    // Delete a Dispense record by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDispense(@PathVariable String id) {
        dispenseService.deleteDispense(id);
        return ResponseEntity.noContent().build();
    }

    // Fetch dispenses between a date range
    @GetMapping("/fetchReports")
    public ResponseEntity<List<Dispense>> fetchReports(@RequestParam("start") String start,
                                                       @RequestParam("end") String end) {
        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        List<Dispense> dispenses = dispenseService.fetchReports(startDate, endDate);
        return ResponseEntity.ok(dispenses);
    }
}
