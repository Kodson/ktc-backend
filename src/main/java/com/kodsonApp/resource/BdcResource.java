package com.kodsonApp.resource;

import com.kodsonApp.domain.Bdc;
import com.kodsonApp.service.BdcService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/bdc"})
@RequiredArgsConstructor
public class BdcResource {
    private final BdcService bdcService;

    @PostMapping
    public ResponseEntity<Bdc> createContact(@RequestBody Bdc bdc) {
        System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/bdc/bdcID")).body(bdcService.createBdc(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<Bdc>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(bdcService.getAllBdcs(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bdc> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(bdcService.getBdc(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        bdcService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
