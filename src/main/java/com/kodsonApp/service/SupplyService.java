package com.kodsonApp.service;

import com.kodsonApp.domain.Supply;
import com.kodsonApp.repository.SupplyRepo;
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
public class SupplyService {
    @Autowired
    private final SupplyRepo supplyRepo;

    public Page<Supply> getAllSupplies(int page, int size) {
        return supplyRepo.findAll(PageRequest.of(page, size));
    }

    public Supply getSupply(String id) {
        return supplyRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Supply createSupply(Supply supply) {
        return supplyRepo.save(supply);
    }

    public void deleteExpense(String id) {
        supplyRepo.deleteById(id);
    }

    public List<Supply> getSupplyByStation(String station) {
        return supplyRepo.findByStation(station);
    }
}
