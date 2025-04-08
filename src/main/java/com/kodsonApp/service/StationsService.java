package com.kodsonApp.service;

import com.kodsonApp.domain.Stations;
import com.kodsonApp.repository.StationsRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class StationsService {
    @Autowired
    private final StationsRepo stationsRepo;

    public Page<Stations> getAllStations(int page, int size) {
        return stationsRepo.findAll(PageRequest.of(page, size, Sort.by("station")));
    }

    public Stations getStation(String id) {
        return stationsRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Stations createBdc(Stations station) {
        return stationsRepo.save(station);
    }

    public Stations assignManager(String stationId, String managerId) {
        Stations station = getStation(stationId);
        station.setManager(managerId);
        return stationsRepo.save(station);
    }

    public Stations findStationByUsername(String username) {
        // Implement the logic to fetch station by username
        // For example, assuming the repository has a method findByManager
        return stationsRepo.findByManager(username).orElseThrow(() -> new RuntimeException("Station not found"));
    }

    public void deleteExpense(String id) {
        stationsRepo.deleteById(id);
    }

}
