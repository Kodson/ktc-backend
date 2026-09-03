package com.kodsonApp.resource;

import com.kodsonApp.domain.MedicalBill;
import com.kodsonApp.service.MedicalBillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api2/medicalBill")
public class MedicalBillResource {

    @Autowired
    private MedicalBillService medicalBillService;

    // Create a medical bill
    @PostMapping
    public ResponseEntity<MedicalBill> createMedicalBill(@RequestBody MedicalBill medicalBill) {
        return ResponseEntity.ok(medicalBillService.saveMedicalBill(medicalBill));
    }

    // Get all medical bills
    @GetMapping
    public ResponseEntity<List<MedicalBill>> getAllMedicalBills() {
        return ResponseEntity.ok(medicalBillService.getAllMedicalBills());
    }

    // Get a medical bill by ID
    @GetMapping("/{id}")
    public ResponseEntity<MedicalBill> getMedicalBillById(@PathVariable String id) {
        return medicalBillService.getMedicalBillById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update a medical bill
    @PutMapping("/{id}")
    public ResponseEntity<MedicalBill> updateMedicalBill(@PathVariable String id, @RequestBody MedicalBill medicalBill) {
        MedicalBill updatedBill = medicalBillService.updateMedicalBill(id, medicalBill);
        return updatedBill != null ? ResponseEntity.ok(updatedBill) : ResponseEntity.notFound().build();
    }

    // Delete a medical bill
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalBill(@PathVariable String id) {
        medicalBillService.deleteMedicalBill(id);
        return ResponseEntity.noContent().build();
    }

    // Reporting: Get bills between two dates
    @GetMapping("/report")
    public ResponseEntity<List<MedicalBill>> getMedicalBillsBetweenDates(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(medicalBillService.getMedicalBillsBetweenDates(startDate, endDate));
    }

    // New endpoint for returning a medical bill
    @PutMapping("/return/{id}")
    public ResponseEntity<MedicalBill> returnMedicalBill(@PathVariable String id, @RequestParam String days, @RequestParam double bill) {
        MedicalBill updatedBill = medicalBillService.returnMedicalBill(id, days, bill);
        return updatedBill != null ? ResponseEntity.ok(updatedBill) : ResponseEntity.notFound().build();
    }
}
