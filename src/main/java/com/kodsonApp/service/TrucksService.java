package com.kodsonApp.service;

import com.kodsonApp.domain.Trucks;
import com.kodsonApp.repository.TrucksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrucksService {

    @Autowired
    private TrucksRepo trucksRepo;

    public Trucks saveTruck(Trucks truck) {
        truck.setStatus("No BVO assigned");
        return trucksRepo.save(truck);
    }

    public List<Trucks> getAllTrucks() {
        return trucksRepo.findAll();
    }

    public Trucks assignBvo(String id, String bvoId, String bvo) {
        Trucks truck = trucksRepo.findById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setBvoId(bvoId);
        truck.setStatus("Truck has BVO but no assistant");
        truck.setBvo(bvo);
        return trucksRepo.save(truck);
    }

    public Trucks assignAssistant(String id, String assistantId, String bvoAss) {
        Trucks truck = trucksRepo.findById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setAssistantId(assistantId);
        truck.setStatus("Truck has both BVO and assistant");
        truck.setBvoAss(bvoAss);
        return trucksRepo.save(truck);
    }

    public void unassignBvo(String id) {
        Trucks truck = trucksRepo.findById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setBvoId(null);
        truck.setStatus("No BVO assigned");
        truck.setBvo(null);
        trucksRepo.save(truck);
    }

    public void unassignAssistant(String id) {
        Trucks truck = trucksRepo.findById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setAssistantId(null);
        truck.setStatus("Truck has BVO but no assistant");
        truck.setBvoAss(null);
    trucksRepo.save(truck);
    }

    public void deleteTruck(String id) {
        trucksRepo.deleteById(id);
    }

    public Trucks updateTruck(String id, Trucks updatedTruck) {
        Trucks truck = trucksRepo.findById(id).orElseThrow(() -> new RuntimeException("Truck not found"));
        truck.setBrv(updatedTruck.getBrv());
        return trucksRepo.save(truck);
    }
}
