package com.kodsonApp.resource;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.service.DailySalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/dailysales"})
@RequiredArgsConstructor
public class DailySalesResource {
    private final DailySalesService dailySalesService;

    @PostMapping
    public ResponseEntity<DailySales> createContact(@RequestBody DailySales bdc) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/dailysales/dailysalesID")).body(dailySalesService.createDailySales(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<DailySales>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(dailySalesService.getAllDailySales(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DailySales> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(dailySalesService.getDailySales(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<DailySales>> getDailySalesByStation(@PathVariable String station) {
        return ResponseEntity.ok().body(dailySalesService.getSaleByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        dailySalesService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
