package com.kodsonApp.service;

import com.kodsonApp.domain.CreditSales;
import com.kodsonApp.repository.CreditSalesRepo;
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
public class CreditSalesService {
    private final CreditSalesRepo creditSalesRepo;

    public Page<CreditSales> getAllCreditSales(int page, int size) {
        return creditSalesRepo.findAll(PageRequest.of(page, size));
    }

    public CreditSales getCreditSales(String id) {
        return creditSalesRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public CreditSales createBdc(CreditSales bdc) {
        return creditSalesRepo.save(bdc);
    }

    public void deleteExpense(String id) {
        creditSalesRepo.deleteById(id);
    }


    public List<CreditSales> getCreditSalesByStation(String station) {
        return creditSalesRepo.findByStation(station);
    }
}
