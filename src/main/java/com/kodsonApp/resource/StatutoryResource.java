package com.kodsonApp.resource;

import com.kodsonApp.domain.Statutory;
import com.kodsonApp.service.StatutoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
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

    @GetMapping("/station/{stationId}")
    public ResponseEntity<Page<Statutory>> getStatutoryByStation(
            @PathVariable String stationId,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryByStation(stationId, page, size, sortDirection));
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<Statutory>> getStatutoryByType(@PathVariable String type) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryByType(type));
    }

    @GetMapping("/payment-status/{paymentStatus}")
    public ResponseEntity<List<Statutory>> getStatutoryByPaymentStatus(@PathVariable String paymentStatus) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryByPaymentStatus(paymentStatus));
    }

    @GetMapping("/station/{stationId}/status/{status}")
    public ResponseEntity<List<Statutory>> getStatutoryByStationAndStatus(@PathVariable String stationId, @PathVariable String status) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryByStationAndStatus(stationId, status));
    }

    @GetMapping("/expiring-before/{date}")
    public ResponseEntity<List<Statutory>> getStatutoryExpiringBefore(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryExpiringBefore(date));
    }

    @GetMapping("/expiring-between")
    public ResponseEntity<List<Statutory>> getStatutoryExpiringBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok().body(statutoryService.getStatutoryExpiringBetween(startDate, endDate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatutory(@PathVariable(value = "id") String id) {
        statutoryService.deleteStatutory(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statutory> updateStatutory(@RequestBody Statutory statutoryDetails, @PathVariable String id) {
        Statutory updatedStatutory = statutoryService.updateStatutory(statutoryDetails, id);
        return ResponseEntity.ok(updatedStatutory);
    }
}
