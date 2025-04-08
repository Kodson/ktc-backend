package com.kodsonApp.resource;

import com.kodsonApp.domain.Expenses;
import com.kodsonApp.service.ExpensesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","mobik/expenses"})
@RequiredArgsConstructor
public class ExpensesResource {
    private final ExpensesService expensesService;

    @PostMapping
    public ResponseEntity<Expenses> createContact(@RequestBody Expenses bdc) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/mobik/expenses/expensesID")).body(expensesService.createExpenses(bdc));
    }

    @GetMapping
    public ResponseEntity<Page<Expenses>> getExpenses(@RequestParam(value = "page", defaultValue = "0") int page,
                                             @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(expensesService.getAllExpenses(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expenses> getExpense(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(expensesService.getExpenses(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<Expenses>> getExpensesByStation(@PathVariable String station) {

        return ResponseEntity.ok().body(expensesService.getExpensesByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        expensesService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
