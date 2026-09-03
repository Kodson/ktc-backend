package com.kodsonApp.resource;
import com.kodsonApp.domain.NpaPrice;
import com.kodsonApp.service.NpaPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = { "/","api2/npa"})
@RequiredArgsConstructor
public class NpaPriceResource {
    private final NpaPriceService npaPriceService;

    @PostMapping
    public ResponseEntity<NpaPrice> createNpa(@RequestBody NpaPrice npaPrice) {
        return ResponseEntity.created(URI.create("/api2/npa/npaID")).body(npaPriceService.createProfit(npaPrice));
    }

    @GetMapping
    public ResponseEntity<List<NpaPrice>> getAllProfits() {
        List<NpaPrice> profits = npaPriceService.getAllProfits();
        return new ResponseEntity<>(profits, OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NpaPrice> getProfit(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(npaPriceService.getProfit(id));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfit(@PathVariable(value = "id") String id) {
        npaPriceService.deleteProfit(id);
        return ResponseEntity.noContent().build();
    }
}
