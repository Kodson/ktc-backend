package com.kodsonApp.service;

import com.kodsonApp.domain.Bdc;
import com.kodsonApp.repository.BdcRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class BdcService {
    @Autowired
    private final BdcRepo bdcRepo;

    public Page<Bdc> getAllBdcs(int page, int size) {
        return bdcRepo.findAll(PageRequest.of(page, size));
    }

    public Bdc getBdc(String id) {
        return bdcRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Bdc createBdc(Bdc bdc) {
        return bdcRepo.save(bdc);
    }

    public void deleteExpense(String id) {
        bdcRepo.deleteById(id);
    }


}
