package com.kodsonApp.resource;
import com.kodsonApp.DTO.AssignManagerRequest;
import com.kodsonApp.DTO.StationDTO;
import com.kodsonApp.domain.Stations;
import com.kodsonApp.service.StationsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","api2/stations"})
@RequiredArgsConstructor
@Slf4j
public class StationsResource {
    private final StationsService stationsService;

    @PostMapping
    public ResponseEntity<Stations> createContact(@RequestBody Stations stations) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/stations/stationsID")).body(stationsService.createBdc(stations));
    }

    @GetMapping
    public ResponseEntity<Page<StationDTO>> getStations(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(stationsService.getAllStations(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Stations> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(stationsService.getStation(id));
    }


    @PostMapping("/assign/{stationId}")
    public ResponseEntity<Stations> assignManager(
            @PathVariable("stationId") String stationId,
            @RequestBody AssignManagerRequest request) {

        // Print full request for debugging
        System.out.println("Received Request: " + request);

        Long managerId = Long.valueOf(request.getManagerDetails().getManagerUserId());

        return ResponseEntity.ok()
                .body(stationsService.assignManager(stationId, managerId));
    }

    @PostMapping("/unassign/{stationId}")
    public ResponseEntity<Stations> unassignManager(@PathVariable("stationId") String stationId) {
        return ResponseEntity.ok()
                .body(stationsService.unassignManager(stationId));
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<Stations> getStationByUser(@PathVariable String username) {
        Stations station = stationsService.findStationByUsername(username);
        return ResponseEntity.ok(station);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        stationsService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }

}
