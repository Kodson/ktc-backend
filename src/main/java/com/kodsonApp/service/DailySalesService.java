package com.kodsonApp.service;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.repository.DailySalesRepo;
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
public class DailySalesService {
    @Autowired
    private final DailySalesRepo dailySalesRepo;

    public Page<DailySales> getAllDailySales(int page, int size) {
        return dailySalesRepo.findAll(PageRequest.of(page, size));
    }

    public DailySales getDailySales(String id) {
        return dailySalesRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public DailySales createDailySales(DailySales dailySales) {
        return dailySalesRepo.save(dailySales);
    }

    public void deleteExpense(String id) {
        dailySalesRepo.deleteById(id);
    }

    public List<DailySales> getSaleByStation(String station) {
        return dailySalesRepo.findByStation(station);
    }
}
