package com.kodsonApp.resource;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trips")
public class TripsResource {

    @Autowired
    private TripService tripService;

    @PostMapping("/save")
    public ResponseEntity<Trips> saveTrip(@RequestBody Trips trip) {
        Trips savedTrip = tripService.saveTrip(trip);
        return ResponseEntity.ok(savedTrip);
    }

    @PutMapping("/updateTrips/{id}")
    public ResponseEntity<Trips> updateTrip(@PathVariable String id, @RequestBody Trips trip) {
        Trips updatedTrip = tripService.updateTrip(id, trip);
        return ResponseEntity.ok(updatedTrip);
    }


    @PutMapping("/closeTrips/{id}")
    public ResponseEntity<Trips> closeTrip(@PathVariable String id, @RequestBody Trips tripDetails) {
        Trips closedTrip = tripService.closeTrip(id, tripDetails);
        return ResponseEntity.ok(closedTrip);
    }

    @GetMapping("/getTrips/{id}")
    public ResponseEntity<Optional<Trips>> getTrip(@PathVariable String id) {
        Optional<Trips> trip = tripService.getTripById(id);
        return ResponseEntity.ok(trip);
    }

    @DeleteMapping("/deleteTrips/{id}")
    public ResponseEntity<Void> deleteTrip(@PathVariable String id) {
        tripService.deleteTrip(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllTrips")
    public ResponseEntity<List<Trips>> getAllTrips() {
        List<Trips> trips = tripService.getAllTrips();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/fetchReports")
    public ResponseEntity<List<Trips>> fetchReports(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Trips> reports = tripService.getTripsByDateRange(startDate, endDate);
        return ResponseEntity.ok(reports);
    }

    @GetMapping("/getFilterByWaybill")
    public ResponseEntity<List<Trips>> getFilterByWaybill() {
        List<Trips> filteredTrips = tripService.getFilteredByWaybill();
        return ResponseEntity.ok(filteredTrips);
    }
}
