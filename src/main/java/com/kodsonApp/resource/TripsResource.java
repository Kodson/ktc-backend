package com.kodsonApp.resource;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api2/trips")
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
    public ResponseEntity<Page<Trips>> getAllTrips(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Trips> variablesPage = tripService.getAllTrips(page,size,sortDirection);
        return ResponseEntity.ok(variablesPage);
    }

    @GetMapping("/fetchReports")
    public ResponseEntity<Page<Trips>> fetchReports(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, sortDirection.equals("desc") ?
                org.springframework.data.domain.Sort.by("date").descending() :
                org.springframework.data.domain.Sort.by("date").ascending());
        Page<Trips> reportsPage = tripService.getTripsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(reportsPage);
    }

    @GetMapping("/getFilterByWaybill")
    public ResponseEntity<Page<Trips>> getFilterByWaybill(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Page<Trips> variablesPage = tripService.getFilteredByWaybill(page,size,sortDirection);
        return ResponseEntity.ok(variablesPage);
    }

    @GetMapping("/searchTrips")
    public ResponseEntity<Page<Trips>> searchVariables(
            @RequestParam(required = false) String brv,
            @RequestParam(required = false) String wayBillNum,
            @RequestParam(required = false) String bvo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, sortDirection.equals("desc") ?
                org.springframework.data.domain.Sort.by("date").descending() :
                org.springframework.data.domain.Sort.by("date").ascending());
        Page<Trips> variablesPage = tripService.searchTrips(brv, wayBillNum, bvo, pageable);
        return ResponseEntity.ok(variablesPage);
    }
}
