package com.kodsonApp.resource;

import com.kodsonApp.domain.Pump;
import com.kodsonApp.service.PumpService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/pump"})
@RequiredArgsConstructor
public class PumpResource {
    private final PumpService pumpService;

    @PostMapping
    public ResponseEntity<Pump> createPump(@RequestBody Pump bdc) {
        return ResponseEntity.created(URI.create("/mobik/pump/pumpID")).body(pumpService.createPump(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<Pump>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                                    @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(pumpService.getAllPumps(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pump> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(pumpService.getPump(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Pump>> getPumpByStation(@PathVariable String station) {
        return ResponseEntity.ok().body(pumpService.getPumpByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        pumpService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
