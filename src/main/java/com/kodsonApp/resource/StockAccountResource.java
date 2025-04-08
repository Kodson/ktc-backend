package com.kodsonApp.resource;

import com.kodsonApp.domain.StockAccount;
import com.kodsonApp.service.StockAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/stockaccount"})
@RequiredArgsConstructor

public class StockAccountResource {
    private final StockAccountService stockAccountService;

    @PostMapping
    public ResponseEntity<StockAccount> createStockAccount(@RequestBody StockAccount stockAccount) {
        //return ResponseEntity.ok().body(stockAccountService.createBdc(stockAccount));
        System.out.println(stockAccount.getId()+ " "+ stockAccount.getReceived()+" "+stockAccount.getOpenStock());
        return ResponseEntity.created(URI.create("/mobik/stockaccount/stockaccountID")).body(stockAccountService.createBdc(stockAccount));
    }

    @GetMapping
    public ResponseEntity<Page<StockAccount>> getBdcs(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(stockAccountService.getAllStockAccount(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockAccount> getBdc(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(stockAccountService.getStockAccount(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<StockAccount>> getStockAccountByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(stockAccountService.getStockAccountByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        stockAccountService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
