package com.kodsonApp.service;

import com.kodsonApp.domain.MedicalBill;
import com.kodsonApp.repository.MedicalBillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalBillService {

    @Autowired
    private MedicalBillRepo medicalBillRepo;

    // Create a medical bill with default status 'At hospital'
    public MedicalBill saveMedicalBill(MedicalBill medicalBill) {
        medicalBill.setStatus("At hospital"); // Set default status
        return medicalBillRepo.save(medicalBill);
    }

    // Update medical bill with return data (days, bill, status)
    public MedicalBill returnMedicalBill(String id, String days, double bill) {
        Optional<MedicalBill> optionalMedicalBill = medicalBillRepo.findById(id);
        if (optionalMedicalBill.isPresent()) {
            MedicalBill medicalBill = optionalMedicalBill.get();
            medicalBill.setDays(days);
            medicalBill.setBill(bill);
            medicalBill.setStatus("Returned"); // Automatically set status to 'Returned'
            return medicalBillRepo.save(medicalBill);
        }
        return null;
    }

    // Get a medical bill by ID
    public Optional<MedicalBill> getMedicalBillById(String id) {
        return medicalBillRepo.findById(id);
    }

    // Get all medical bills
    public List<MedicalBill> getAllMedicalBills() {
        return medicalBillRepo.findAll();
    }

    // Update a medical bill
    public MedicalBill updateMedicalBill(String id, MedicalBill updatedBill) {
        if (medicalBillRepo.existsById(id)) {
            updatedBill.setId(id);
            return medicalBillRepo.save(updatedBill);
        }
        return null;
    }

    // Delete a medical bill
    public void deleteMedicalBill(String id) {
        medicalBillRepo.deleteById(id);
    }

    // Reporting between two dates
    public List<MedicalBill> getMedicalBillsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return medicalBillRepo.findByDateBetween(startDate, endDate);
    }
}
