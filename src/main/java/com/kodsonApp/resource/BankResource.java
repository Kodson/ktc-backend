package com.kodsonApp.resource;

import com.kodsonApp.domain.Bank;
import com.kodsonApp.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/bank"})
@RequiredArgsConstructor
public class BankResource {
    private final BankService bankService;

    @PostMapping
    public ResponseEntity<Bank> createBank(@RequestBody Bank bank) {
        return ResponseEntity.created(URI.create("/mobik/bank/bankID")).body(bankService.createBank(bank));
    }

    @GetMapping
    public ResponseEntity<Page<Bank>> getBanks(@RequestParam(value = "page", defaultValue = "0") int page,
                                                       @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(bankService.getAllBanks(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bank> getBank(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(bankService.getBank(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Bank>> getBankByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(bankService.getBankByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        bankService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
