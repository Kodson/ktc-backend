package com.kodsonApp.resource;

import com.kodsonApp.domain.Supply;
import com.kodsonApp.service.SupplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","api/supply"})
@RequiredArgsConstructor
public class SuuplyResource {
    private final SupplyService supplyService;

    @PostMapping
    public ResponseEntity<Supply> createSupply(@RequestBody Supply supply) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/api/supply/supplyID")).body(supplyService.createSupply(supply));
    }

    @GetMapping
    public ResponseEntity<Page<Supply>> getSupplies(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(supplyService.getAllSupplies(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Supply> getSupply(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(supplyService.getSupply(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Supply>> getSupplyByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(supplyService.getSupplyByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        supplyService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
