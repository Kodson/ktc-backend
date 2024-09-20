package com.kodsonApp.service;

import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.PayRoll;
import com.kodsonApp.repository.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {
    @Autowired
    private LoanRepo loanRepo;

    public Loans createLoan(Loans loan) {
        return loanRepo.save(loan);
    }

    public List<Loans> getAllLoans() {
        return loanRepo.findAll();
    }

    public Optional<Loans> getLoanById(String id) {
        return loanRepo.findById(id);
    }

    public Loans updateLoan(String id, Loans loanDetails) {
        Loans loan = loanRepo.findById(id).orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setName(loanDetails.getName());
        loan.setDate(loanDetails.getDate());
        loan.setDescription(loanDetails.getDescription());
        loan.setAmount(loanDetails.getAmount());
        return loanRepo.save(loan);
    }

    public void deleteLoan(String id) {
        loanRepo.deleteById(id);
    }

    public List<Loans> getPayrollByEmployee(String employeeId) {
        return loanRepo.findByEmployeeId(employeeId);
    }
}
