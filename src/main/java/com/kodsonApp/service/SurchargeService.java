package com.kodsonApp.service;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.Surcharge;
import com.kodsonApp.repository.LoanRepo;
import com.kodsonApp.repository.SurchargeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SurchargeService {
    @Autowired
    private SurchargeRepo surchargeRepo;

    public Surcharge createSurcharge(Surcharge surcharge) {
        return surchargeRepo.save(surcharge);
    }

    public List<Surcharge> getAllSurcharge() {
        return surchargeRepo.findAll();
    }

    public Optional<Surcharge> getSurchargeById(String id) {
        return surchargeRepo.findById(id);
    }

    public Surcharge updateSurcharge(String id, Surcharge surchargeDetails) {
        Surcharge surcharge = surchargeRepo.findById(id).orElseThrow(() -> new RuntimeException("Surcharge not found"));
        surcharge.setName(surchargeDetails.getName());
        surcharge.setDate(surchargeDetails.getDate());
        surcharge.setDescription(surchargeDetails.getDescription());
        surcharge.setAmount(surchargeDetails.getAmount());
        return surchargeRepo.save(surcharge);
    }

    public void deleteSurcharge(String id) {
        surchargeRepo.deleteById(id);
    }
    public List<Surcharge> getPayrollByEmployee(String employeeId) {
        return surchargeRepo.findByEmployeeId(employeeId);
    }
}
