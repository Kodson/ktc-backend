package com.kodsonApp.service;

import com.kodsonApp.domain.Brv;
import com.kodsonApp.repository.BrvRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class BrvService {
    @Autowired
    private final BrvRepo brvRepo;

    public Page<Brv> getAllBdcs(int page, int size) {
        return brvRepo.findAll(PageRequest.of(page, size, Sort.by("brvNumber")));
    }

    public Brv getBdc(String id) {
        return brvRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Brv createBdc(Brv bdc) {
        return brvRepo.save(bdc);
    }

    public void deleteExpense(String id) {
        brvRepo.deleteById(id);
    }
}
