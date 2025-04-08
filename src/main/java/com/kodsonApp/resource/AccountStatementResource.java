package com.kodsonApp.resource;

import com.kodsonApp.domain.AccountStatement;
import com.kodsonApp.service.AccountStatementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = {"/", "mobik/statement"})
@RequiredArgsConstructor
public class AccountStatementResource {
    private final AccountStatementService accountStatementService;

    @PostMapping
    public ResponseEntity<AccountStatement> createStatement(@RequestBody AccountStatement statement) {
        return ResponseEntity.created(URI.create("/mobik/statement/statementID")).body(accountStatementService.createStatement(statement));
    }

    @GetMapping
    public ResponseEntity<Page<AccountStatement>> getStatements(@RequestParam(value = "page", defaultValue = "0") int page,
                                                                @RequestParam(value = "size", defaultValue = "20") int size) {
        return ResponseEntity.ok().body(accountStatementService.getAllStatements(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountStatement> getStatement(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(accountStatementService.getStatement(id));
    }

    @GetMapping("/station/{station}")
    public ResponseEntity<List<AccountStatement>> getStatementByStation(@PathVariable String station) {
        return ResponseEntity.ok().body(accountStatementService.getStatementByStation(station));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable(value = "id") String id) {
        accountStatementService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }


}
