package com.kodsonApp.service;

import com.kodsonApp.domain.Dispense;
import com.kodsonApp.repository.DispenseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DispenseService {

    @Autowired
    private DispenseRepo dispenseRepo;

    // Save or create a new Dispense record
    public Dispense saveDispense(Dispense dispense) {
        return dispenseRepo.save(dispense);
    }

    // Get all Dispense records
    public List<Dispense> getAllDispenses() {
        return dispenseRepo.findAll();
    }

    // Get a specific Dispense record by ID
    public Optional<Dispense> getDispenseById(String id) {
        return dispenseRepo.findById(id);
    }

    // Delete a Dispense record by ID
    public void deleteDispense(String id) {
        dispenseRepo.deleteById(id);
    }

    // Update an existing Dispense record or create a new one if it doesn't exist
    public Dispense updateDispense(String id, Dispense updatedDispense) {
        return dispenseRepo.findById(id)
                .map(dispense -> {
                    dispense.setDate(updatedDispense.getDate());
                    dispense.setFromLocation(updatedDispense.getFromLocation());
                    dispense.setDestination(updatedDispense.getDestination());
                    dispense.setBrv(updatedDispense.getBrv());
                    dispense.setDistance(updatedDispense.getDistance());
                    dispense.setQuantity(updatedDispense.getQuantity());
                    dispense.setConstant(updatedDispense.getConstant());
                    dispense.setBvo(updatedDispense.getBvo());
                    dispense.setOmc(updatedDispense.getOmc());
                    dispense.setPurpose(updatedDispense.getPurpose());
                    return dispenseRepo.save(dispense);
                })
                .orElseGet(() -> {
                    updatedDispense.setId(id);
                    return dispenseRepo.save(updatedDispense);
                });
    }
}
