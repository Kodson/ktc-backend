package com.kodsonApp.resource;
import com.kodsonApp.domain.WashingBay;
import com.kodsonApp.service.WashinBayService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","api/washingBay"})
@RequiredArgsConstructor
public class WashingBayResource {
    private final WashinBayService washinBayService;

    @PostMapping
    public ResponseEntity<WashingBay> createWashingBay(@RequestBody WashingBay washingBay) {
        return ResponseEntity.created(URI.create("/api/washingBay/washingBayID")).body(washinBayService.createWashingBay(washingBay));
    }

    @GetMapping
    public ResponseEntity<Page<WashingBay>> getAllWashingBay(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "1000") int size,
                                                      @RequestParam(defaultValue = "desc") String sortDirection){
        return ResponseEntity.ok().body(washinBayService.getAllWashingBay(page, size, sortDirection));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WashingBay> getWashingBay(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(washinBayService.getWashingBayById(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<WashingBay>> getWashingBayByStation(@PathVariable String station) {
        return ResponseEntity.ok().body(washinBayService.getWashingBayByUser(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWashingBay(@PathVariable(value = "id") String id) {
        washinBayService.deleteWashingBay(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<WashingBay> updateWashingBay( @RequestBody WashingBay washingBayDetails, @PathVariable String id) {
        WashingBay updatedWashingBay = washinBayService.updateWashingBay(washingBayDetails, id);
        return ResponseEntity.ok(updatedWashingBay);
    }

}
