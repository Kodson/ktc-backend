package com.kodsonApp.resource;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.Surcharge;
import com.kodsonApp.service.SurchargeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/surcharge")
public class SurchargeResource {
    @Autowired
    private SurchargeService surchargeService;

    @PostMapping
    public Surcharge createLoan(@RequestBody Surcharge surcharge) {
        return surchargeService.createSurcharge(surcharge);
    }

    @GetMapping
    public List<Surcharge> getAllSurcharge() {
        return surchargeService.getAllSurcharge();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Surcharge> getSurchargeById(@PathVariable String id) {
        Surcharge surcharge = surchargeService.getSurchargeById(id).orElseThrow(() -> new RuntimeException("Surcharge not found"));
        return ResponseEntity.ok(surcharge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Surcharge> updateSurcharge(@PathVariable String id, @RequestBody Surcharge surchargeDetails) {
        Surcharge updatedSurcharge = surchargeService.updateSurcharge(id, surchargeDetails);
        return ResponseEntity.ok(updatedSurcharge);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSurcharge(@PathVariable String id) {
        surchargeService.deleteSurcharge(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Surcharge>> getPayrollByEmployee(@PathVariable String employeeId) {
        List<Surcharge> payrolls = surchargeService.getPayrollByEmployee(employeeId);
        return ResponseEntity.ok(payrolls);
    }
}
