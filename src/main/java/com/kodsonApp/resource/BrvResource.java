package com.kodsonApp.resource;

import com.kodsonApp.domain.Brv;
import com.kodsonApp.service.BrvService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/brv"})
@RequiredArgsConstructor
public class BrvResource {
    private final BrvService brvService;

    @PostMapping
    public ResponseEntity<Brv> createContact(@RequestBody Brv bdc) {
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/bdc/bdcID")).body(brvService.createBdc(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<Brv>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "40") int size) {
        return ResponseEntity.ok().body(brvService.getAllBdcs(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Brv> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(brvService.getBdc(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        brvService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
