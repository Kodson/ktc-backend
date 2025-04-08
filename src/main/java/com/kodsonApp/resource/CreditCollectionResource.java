package com.kodsonApp.resource;

import com.kodsonApp.domain.CreditCollection;
import com.kodsonApp.service.CreditCollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/creditcollection"})
@RequiredArgsConstructor
public class CreditCollectionResource {
    private final CreditCollectionService creditCollectionService;

    @PostMapping
    public ResponseEntity<CreditCollection> createCreditCollection(@RequestBody CreditCollection bdc) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/creditcollection/creditcollectionID")).body(creditCollectionService.createBdc(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<CreditCollection>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(creditCollectionService.getAllCreditCollections(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreditCollection> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(creditCollectionService.getCreditCollection(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<CreditCollection>> getCreditCollectionByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(creditCollectionService.getCreditCollectionByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        creditCollectionService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
