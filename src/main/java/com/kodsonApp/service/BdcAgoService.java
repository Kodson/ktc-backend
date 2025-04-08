package com.kodsonApp.service;

import com.kodsonApp.domain.BdcAgo;
import com.kodsonApp.repository.BdcAgoRepo;
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
public class BdcAgoService {
    @Autowired
    private final BdcAgoRepo bdcAgoRepo;

    public Page<BdcAgo> getAllBdcAgo(int page, int size) {
        return bdcAgoRepo.findAll(PageRequest.of(page, size, Sort.by("qty")));
    }

    public BdcAgo getBdcPms(String id) {
        return bdcAgoRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public BdcAgo createBdcpms(BdcAgo bdc) {
        return bdcAgoRepo.save(bdc);
    }

    public BdcAgo updateBdcAgo(String id, BdcAgo bdcDetails) {
        BdcAgo bdc = bdcAgoRepo.findById(id).orElseThrow(() -> new RuntimeException("BDC not found"));
        bdc.setDate(bdcDetails.getDate());
        bdc.setBdc_Name(bdcDetails.getBdc_Name());
        bdc.setQty(bdcDetails.getQty());
        bdc.setPrice(bdcDetails.getPrice());
        bdc.setProduct(bdcDetails.getProduct());
        return bdcAgoRepo.save(bdc);
    }

    public void deleteBdcAgo(String id) {
        bdcAgoRepo.deleteById(id);
    }
}
