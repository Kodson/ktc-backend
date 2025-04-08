package com.kodsonApp.resource;

import com.kodsonApp.domain.CreditSales;
import com.kodsonApp.service.CreditSalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/creditsales"})
@RequiredArgsConstructor
public class CreditSalesResource {
    private final CreditSalesService creditSalesService;

    @PostMapping
    public ResponseEntity<CreditSales> createContact(@RequestBody CreditSales creditSales) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/creditsales/creditsalesID")).body(creditSalesService.createBdc(creditSales));
    }

    @GetMapping
    public ResponseEntity<Page<CreditSales>> getAllCreditSales(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(creditSalesService.getAllCreditSales(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditSales> getCreditSales(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(creditSalesService.getCreditSales(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<CreditSales>> getCreditSalesByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(creditSalesService.getCreditSalesByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        creditSalesService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
