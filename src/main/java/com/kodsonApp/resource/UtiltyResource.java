package com.kodsonApp.resource;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.domain.Loans;
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

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Utility>> getUtilityByStation(@PathVariable String station) {
        return ResponseEntity.ok().body(utilityService.getUtilityByUser(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtility(@PathVariable(value = "id") String id) {
        utilityService.deleteUtility(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utility> updateLoan( @RequestBody Utility utilityDetails, @PathVariable String id) {
        Utility updatedUtility = utilityService.updateUtility(utilityDetails, id);
        return ResponseEntity.ok(updatedUtility);
    }

}
