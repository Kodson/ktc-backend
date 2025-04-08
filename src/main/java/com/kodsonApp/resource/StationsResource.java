package com.kodsonApp.resource;
import com.kodsonApp.domain.Stations;
import com.kodsonApp.service.StationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/stations"})
@RequiredArgsConstructor
public class StationsResource {
    private final StationsService stationsService;

    @PostMapping
    public ResponseEntity<Stations> createContact(@RequestBody Stations stations) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/stations/stationsID")).body(stationsService.createBdc(stations));
    }

    @GetMapping
    public ResponseEntity<Page<Stations>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(stationsService.getAllStations(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stations> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(stationsService.getStation(id));
    }

    @PostMapping("/assign/{stationId}")
    public ResponseEntity<Stations> assignManager(@PathVariable("stationId") String stationId, @RequestBody Stations stations) {
        String managerId = stations.getManager();
        System.out.println("Manager assigned: " + managerId);
        return ResponseEntity.ok().body(stationsService.assignManager(stationId, managerId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Stations> getStationByUser(@PathVariable String username) {
        Stations station = stationsService.findStationByUsername(username);
        return ResponseEntity.ok(station);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        stationsService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
