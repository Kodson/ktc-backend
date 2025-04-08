package com.kodsonApp.resource;

import com.kodsonApp.domain.ProductLifting;
import com.kodsonApp.service.ProductLiftingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(path = { "/","mobik/productLifting"})
@RequiredArgsConstructor
public class ProductLiftingResource {
    private final ProductLiftingService productLiftingService;

    @PostMapping
    public ResponseEntity<ProductLifting> createProductLifting(@RequestBody ProductLifting bdc) {
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/productLifting/productLiftingID")).body(productLiftingService.createProductLifting(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<ProductLifting>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "40") int size) {
        return ResponseEntity.ok().body(productLiftingService.getAllProductLifting(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductLifting> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(productLiftingService.getProductLifting(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductLifting> updateProductLifting(@PathVariable(value = "id") String id, @RequestBody ProductLifting updatedProductLifting) {
        return ResponseEntity.ok().body(productLiftingService.updateProductLifting(id, updatedProductLifting));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductLifting(@PathVariable(value = "id") String id) {
        productLiftingService.deleteProductLifting(id);
        return ResponseEntity.noContent().build();
    }
}
