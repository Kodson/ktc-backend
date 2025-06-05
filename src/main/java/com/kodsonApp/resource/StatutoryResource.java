package com.kodsonApp.resource;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.Statutory;
import com.kodsonApp.domain.Utility;
import com.kodsonApp.service.StatutoryService;
import com.kodsonApp.service.UtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","api/statutory"})
@RequiredArgsConstructor
public class StatutoryResource {
    private final StatutoryService statutoryService;

    @PostMapping
    public ResponseEntity<Statutory> createStatutory(@RequestBody Statutory statutory) {
        return ResponseEntity.created(URI.create("/api/statutory/statutoryID")).body(statutoryService.createStatutory(statutory));
    }

    @GetMapping
    public ResponseEntity<Page<Statutory>> getAllStatutory(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "1000") int size,
                                                      @RequestParam(defaultValue = "desc") String sortDirection){
        return ResponseEntity.ok().body(statutoryService.getAllStatutory(page, size, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statutory> getStatutory(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryById(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Statutory>> getUtilityByUser(@PathVariable String station) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryByUser(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutory(@PathVariable(value = "id") String id) {
        statutoryService.deleteStatutory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statutory> updateStatutory( @RequestBody Statutory statutoryDetails, @PathVariable String id) {
        Statutory updatedStatutory = statutoryService.updateStatutory(statutoryDetails, id);
        return ResponseEntity.ok(updatedStatutory);
    }

}
