package com.kodsonApp.resource;

import com.kodsonApp.domain.Utility;
import com.kodsonApp.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","api/utility"})
@RequiredArgsConstructor
public class UtiltyResource {
    private final UtilityService utilityService;

    @PostMapping
    public ResponseEntity<Utility> createUtility(@RequestBody Utility utility) {
        return ResponseEntity.created(URI.create("/api/utility/utilityID")).body(utilityService.createUtility(utility));
    }

    @GetMapping
    public ResponseEntity<Page<Utility>> getUtilities(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "1000") int size,
                                                      @RequestParam(defaultValue = "desc") String sortDirection){
        return ResponseEntity.ok().body(utilityService.getAllUtility(page, size, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utility> getUtility(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(utilityService.getUtilityById(id));
    }

    @GetMapping("/station/{stationId}")
    public ResponseEntity<Page<Utility>> getUtilityByStation(
            @PathVariable String stationId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "1000") int size,
            @RequestParam(value = "sortDirection", defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok().body(utilityService.getUtilityByStation(stationId, page, size, sortDirection));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Utility>> getUtilityByStatus(@PathVariable String status) {
        return ResponseEntity.ok().body(utilityService.getUtilityByStatus(status));
    }

    @GetMapping("/type/{utilityType}")
    public ResponseEntity<List<Utility>> getUtilityByType(@PathVariable String utilityType) {
        return ResponseEntity.ok().body(utilityService.getUtilityByType(utilityType));
    }

    @GetMapping("/station/{stationId}/status/{status}")
    public ResponseEntity<List<Utility>> getUtilityByStationAndStatus(@PathVariable String stationId, @PathVariable String status) {
        return ResponseEntity.ok().body(utilityService.getUtilityByStationAndStatus(stationId, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtility(@PathVariable(value = "id") String id) {
        utilityService.deleteUtility(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utility> updateUtility(@RequestBody Utility utilityDetails, @PathVariable String id) {
        Utility updatedUtility = utilityService.updateUtility(utilityDetails, id);
        return ResponseEntity.ok(updatedUtility);
    }
}
