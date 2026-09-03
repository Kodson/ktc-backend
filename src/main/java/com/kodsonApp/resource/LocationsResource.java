
package com.kodsonApp.resource;
import com.kodsonApp.domain.Locations;
import com.kodsonApp.service.LocationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api2/locations")
public class LocationsResource {

    @Autowired
    private LocationsService locationsService;

    @GetMapping
    public List<Locations> getAllLocations() {
        return locationsService.getAllLocations();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Locations> getLocationById(@PathVariable String id) {
        Optional<Locations> location = locationsService.getLocationById(id);
        return location.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Locations createLocation(@RequestBody Locations location) {
        return locationsService.saveLocation(location);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Locations> updateLocation(@PathVariable String id, @RequestBody Locations location) {
        if (!locationsService.getLocationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Locations updatedLocation = locationsService.updateLocation(id, location);
        return ResponseEntity.ok(updatedLocation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable String id) {
        if (!locationsService.getLocationById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        locationsService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
}
