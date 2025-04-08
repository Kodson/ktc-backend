package com.kodsonApp.service;

import com.kodsonApp.domain.Distribution;
import com.kodsonApp.repository.DistributionRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class DistributionService {
    @Autowired
    private final DistributionRepo distributionRepo;

    public Page<Distribution> getAllDis(int page, int size) {
        return distributionRepo.findAll(PageRequest.of(page, size, Sort.by("date")));
    }

    public Distribution getDis(String id) {
        return distributionRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Distribution createDis(Distribution distribution) {
        return distributionRepo.save(distribution);
    }

    public Distribution updateDis(Distribution distribution) {
        return distributionRepo.save(distribution);
    }

    public void deleteDis(String id) {
        distributionRepo.deleteById(id);
    }

    public List<Distribution> getDistributionsByStation(String station) {
        return distributionRepo.findByStation(station);
    }
}
