package com.kodsonApp.service;

import com.kodsonApp.domain.NpaPrice;
import com.kodsonApp.repository.NpaPriceRepo;
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
public class NpaPriceService {
    @Autowired
    private final NpaPriceRepo npaPriceRepo;

    public List<NpaPrice> getAllProfits() {
        return npaPriceRepo.findAll();
    }

    public NpaPrice getProfit(String id) {
        return npaPriceRepo.findById(id).orElseThrow(() -> new RuntimeException("Price not found"));
    }

    public NpaPrice createProfit(NpaPrice npaPrice) {
        return npaPriceRepo.save(npaPrice);
    }

    public void deleteProfit(String id) {
        npaPriceRepo.deleteById(id);
    }

}
