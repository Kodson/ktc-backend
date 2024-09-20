
package com.kodsonApp.service;

import com.kodsonApp.domain.Locations;
import com.kodsonApp.repository.LocationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationsService {

    @Autowired
    private LocationsRepo locationsRepo;

    public List<Locations> getAllLocations() {
        return locationsRepo.findAll();
    }

    public Optional<Locations> getLocationById(String id) {
        return locationsRepo.findById(id);
    }

    public Locations saveLocation(Locations location) {
        return locationsRepo.save(location);
    }

    public void deleteLocation(String id) {
        locationsRepo.deleteById(id);
    }

    public Locations updateLocation(String id, Locations location) {
        location.setId(id);
        return locationsRepo.save(location);
    }
}
