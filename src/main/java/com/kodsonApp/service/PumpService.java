package com.kodsonApp.service;

import com.kodsonApp.domain.Pump;
import com.kodsonApp.repository.PumpRepo;
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
public class PumpService {
    @Autowired
    private final PumpRepo pumpRepo;

    public Page<Pump> getAllPumps(int page, int size) {
        return pumpRepo.findAll(PageRequest.of(page, size));
    }

    public Pump getPump(String id) {
        return pumpRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Pump createPump(Pump pump) {
        return pumpRepo.save(pump);
    }

    public void deleteExpense(String id) {
        pumpRepo.deleteById(id);
    }

    public List<Pump> getPumpByStation(String station) {
        return pumpRepo.findByStation(station);
    }
}
