package com.kodsonApp.service;

import com.kodsonApp.domain.StockAccount;
import com.kodsonApp.repository.StockAccountRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class StockAccountService {
    @Autowired
    private final StockAccountRepo stockAccountRepo;

    public Page<StockAccount> getAllStockAccount(int page, int size) {
        return stockAccountRepo.findAll(PageRequest.of(page, size));
    }

    public StockAccount getStockAccount(String id) {
        return stockAccountRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public StockAccount createBdc(StockAccount stockAccount) {
        return stockAccountRepo.save(stockAccount);
    }

    public void deleteExpense(String id) {
        stockAccountRepo.deleteById(id);
    }

    public List<StockAccount> getStockAccountByStation(String station) {
        return stockAccountRepo.findByStation(station);
    }
}
