package com.kodsonApp.service;

import com.kodsonApp.domain.FuelDischarge;
import com.kodsonApp.repository.FuelDischargeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FuelDischargeService {

    @Autowired
    private FuelDischargeRepo fuelDischargeRepo;

    // Create or update a fuel discharge record
    public FuelDischarge saveFuelDischarge(FuelDischarge fuelDischarge) {
        return fuelDischargeRepo.save(fuelDischarge);
    }

    // Retrieve all fuel discharge records
    public List<FuelDischarge> getAllFuelDischarges() {
        return fuelDischargeRepo.findAll();
    }

    // Retrieve a single fuel discharge record by ID
    public Optional<FuelDischarge> getFuelDischargeById(String id) {
        return fuelDischargeRepo.findById(id);
    }

    // Delete a fuel discharge record by ID
    public void deleteFuelDischargeById(String id) {
        fuelDischargeRepo.deleteById(id);
    }
}
