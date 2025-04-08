package com.kodsonApp.service;

import com.kodsonApp.domain.AccountStatement;
import com.kodsonApp.repository.AccountStatementRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class AccountStatementService {
    private final AccountStatementRepo accountStatementRepo;

    public Page<AccountStatement> getAllStatements(int page, int size) {
        return accountStatementRepo.findAll(PageRequest.of(page, size));
    }

    public AccountStatement getStatement(String id) {
        return accountStatementRepo.findById(id).orElseThrow(() -> new RuntimeException("Statement not found"));
    }

    public AccountStatement createStatement(AccountStatement accountStatement) {
        return accountStatementRepo.save(accountStatement);
    }

    public void deleteExpense(String id) {
        accountStatementRepo.deleteById(id);
    }

    public List<AccountStatement> getStatementByStation(String station) {
        return accountStatementRepo.findByStation(station);
    }
}
