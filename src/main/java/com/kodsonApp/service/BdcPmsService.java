package com.kodsonApp.service;

import com.kodsonApp.domain.BdcPms;
import com.kodsonApp.repository.BdcPmsRepo;
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
public class BdcPmsService {
    @Autowired
    private final BdcPmsRepo bdcPmsRepo;

    public Page<BdcPms> getAllBdcPmss(int page, int size) {
        return bdcPmsRepo.findAll(PageRequest.of(page, size));
    }

    public BdcPms getBdcPms(String id) {
        return bdcPmsRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public BdcPms createBdcpms(BdcPms bdc) {
        return bdcPmsRepo.save(bdc);
    }

    public void deleteBdcPms(String id) {
        bdcPmsRepo.deleteById(id);
    }

    public BdcPms updateBdcPms(String id, BdcPms bdc) {
        BdcPms existingBdc = bdcPmsRepo.findById(id).orElseThrow(() -> new RuntimeException("BDC PMS not found"));
        existingBdc.setDate(bdc.getDate());
        existingBdc.setBdc_Name(bdc.getBdc_Name());
        existingBdc.setQty(bdc.getQty());
        existingBdc.setPrice(bdc.getPrice());
        existingBdc.setProduct(bdc.getProduct());
        return bdcPmsRepo.save(existingBdc);
    }
}
