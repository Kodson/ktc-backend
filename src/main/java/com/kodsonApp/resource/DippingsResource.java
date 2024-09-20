package com.kodsonApp.resource;

import com.kodsonApp.domain.Dippings;
import com.kodsonApp.service.DippingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/FuelDippings")
public class DippingsResource {

    @Autowired
    private DippingsService dippingsService;

    @GetMapping
    public List<Dippings> getAllDippings() {
        return dippingsService.getAllDippings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dippings> getDippingById(@PathVariable String id) {
        return dippingsService.getDippingById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Dippings createDipping(@RequestBody Dippings dipping) {
        return dippingsService.saveDipping(dipping);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dippings> updateDipping(@PathVariable String id, @RequestBody Dippings dipping) {
        Dippings updatedDipping = dippingsService.updateDipping(id, dipping);
        if (updatedDipping != null) {
            return ResponseEntity.ok(updatedDipping);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDipping(@PathVariable String id) {
        dippingsService.deleteDipping(id);
        return ResponseEntity.noContent().build();
    }
}
