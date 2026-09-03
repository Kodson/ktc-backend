package com.kodsonApp.resource;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.PayRoll;
import com.kodsonApp.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/loans")
public class LoanResource {
    @Autowired
    private LoanService loanService;

    @PostMapping
    public Loans createLoan(@RequestBody Loans loan) {
        return loanService.createLoan(loan);
    }

    @GetMapping
    public List<Loans> getAllLoans() {
        return loanService.getAllLoans();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loans> getLoanById(@PathVariable String id) {
        Loans loan = loanService.getLoanById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
        return ResponseEntity.ok(loan);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loans> updateLoan(@PathVariable String id, @RequestBody Loans loanDetails) {
        Loans updatedLoan = loanService.updateLoan(id, loanDetails);
        return ResponseEntity.ok(updatedLoan);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable String id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Loans>> getPayrollByEmployee(@PathVariable String employeeId) {
        List<Loans> payrolls = loanService.getPayrollByEmployee(employeeId);
        return ResponseEntity.ok(payrolls);
    }
}
