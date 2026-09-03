package com.kodsonApp.resource;

import com.kodsonApp.domain.Trucks;
import com.kodsonApp.service.TrucksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/trucks")
public class TrucksResource {

    @Autowired
    private TrucksService trucksService;

    @PostMapping
    public Trucks createTruck(@RequestBody Trucks truck) {
        return trucksService.saveTruck(truck);
    }

    @GetMapping
    public List<Trucks> getAllTrucks() {
        return trucksService.getAllTrucks();
    }

    @PostMapping("/assignBvo")
    public Trucks assignBvo(@RequestParam String id, @RequestParam String bvoId, @RequestParam String bvo) {
        return trucksService.assignBvo(id, bvoId, bvo);
    }

    @PostMapping("/assignAss")
    public Trucks assignAssistant(@RequestParam String id, @RequestParam String assistantId, @RequestParam String bvoAss) {
        return trucksService.assignAssistant(id, assistantId , bvoAss);
    }

    @PostMapping("/unAssignBvo")
    public void unassignBvo(@RequestParam String id) {
        trucksService.unassignBvo(id);
    }

    @PostMapping("/unAssignAss")
    public void unassignAssistant(@RequestParam String id) {
        trucksService.unassignAssistant(id);
    }

    @DeleteMapping("/{id}")
    public void deleteTruck(@PathVariable String id) {
        trucksService.deleteTruck(id);
    }

    @PutMapping("/{id}")
    public Trucks updateTruck(@PathVariable String id, @RequestBody Trucks updatedTruck) {
        return trucksService.updateTruck(id, updatedTruck);
    }
}
