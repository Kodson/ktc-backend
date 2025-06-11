package com.kodsonApp.service;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import com.kodsonApp.repository.TripsRepo;
import com.kodsonApp.repository.VariablesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TripService {

    @Autowired
    private TripsRepo tripsRepo;

    @Autowired
    private VariablesRepo variablesRepo;

    public Trips saveTrip(Trips trip) {
        long maxSequence = tripsRepo.findMaxSequence();
        trip.setSequence(maxSequence + 1);
        return tripsRepo.save(trip);
    }

    public Page<Trips> getAllTrips(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "sequence"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return tripsRepo.findAll(pageable);
    }

    public Optional<Trips> getTripById(String id) {
        return tripsRepo.findById(id);
    }

    public Trips updateTrip(String id, Trips updatedTrip) {
        return tripsRepo.findById(id)
                .map(trip -> {
                    trip.setUserName(updatedTrip.getUserName());
                    trip.setBrv(updatedTrip.getBrv());
                    trip.setBvo(updatedTrip.getBvo());
                    trip.setDate(updatedTrip.getDate());
                    trip.setCapacity(updatedTrip.getCapacity());
                    trip.setCustomer(updatedTrip.getCustomer());
                    trip.setProduct(updatedTrip.getProduct());
                    trip.setDateReceived(updatedTrip.getDateReceived());
                    trip.setWayBillNum(updatedTrip.getWayBillNum());
                    trip.setDestination(updatedTrip.getDestination());
                    trip.setQuantityDischarged(updatedTrip.getQuantityDischarged());
                    trip.setShortage(updatedTrip.getShortage());
                    trip.setQuantityRemaining(updatedTrip.getQuantityRemaining());
                    return tripsRepo.save(trip);
                }).orElseThrow(() -> new RuntimeException("Trip not found with id " + id));
    }

    public Trips closeTrip(String id, Trips tripDetails) {
        return tripsRepo.findById(id)
                .map(trip -> {
                    // Set the fields based on modal data
                    trip.setDateReceived(tripDetails.getDateReceived());
                    trip.setWayBillNum(tripDetails.getWayBillNum());
                    trip.setDestination(tripDetails.getDestination());
                    trip.setQuantityDischarged(tripDetails.getQuantityDischarged());
                    trip.setShortage(tripDetails.getShortage());
                    trip.setQuantityRemaining(tripDetails.getQuantityRemaining());
                    // Set the trip status to "Returned"
                    trip.setTripStatus("Returned");
                    return tripsRepo.save(trip);
                }).orElseThrow(() -> new RuntimeException("Trip not found with id " + id));
    }

    public void deleteTrip(String id) {
        tripsRepo.deleteById(id);
    }

    public Page<Trips> getTripsByDateRange(LocalDate startDate, LocalDate endDate,Pageable pageable) {
        return tripsRepo.findByDateBetween(startDate, endDate, pageable);
    }


    public Page<Trips> getFilteredByWaybill(int page, int size, String sortDirection) {
        // Fetch all wayBillNums from Variables
        List<String> variableWayBillNums = variablesRepo.findAll()
                .stream()
                .map(Variables::getWayBillNum)
                .collect(Collectors.toList());

        // Create Pageable for pagination and sorting
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date");
        Pageable pageable = PageRequest.of(page, size, sort);

        // Fetch paginated trips
        Page<Trips> paginatedTrips = tripsRepo.findAll(pageable);

        // Filter trips to exclude those with matching wayBillNums
        List<Trips> filteredTrips = paginatedTrips.getContent()
                .stream()
                .filter(trip -> !variableWayBillNums.contains(trip.getWayBillNum()))
                .collect(Collectors.toList());

        // Return a new Page object with filtered data
        return new PageImpl<>(filteredTrips, pageable, paginatedTrips.getTotalElements());
    }


    public Page<Trips> searchTrips(String brv, String wayBillNum, String bvo, Pageable pageable) {
        return tripsRepo.findByBrvContainingOrWayBillNumContainingOrBvoContaining(
                brv, wayBillNum, bvo, pageable);
    }
}