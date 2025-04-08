package com.kodsonApp.service;

import com.kodsonApp.domain.ProductLifting;
import com.kodsonApp.repository.ProductLiftingRepo;
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
public class ProductLiftingService {
    @Autowired
    private final ProductLiftingRepo productLiftingRepo;

    public Page<ProductLifting> getAllProductLifting(int page, int size) {
        return productLiftingRepo.findAll(PageRequest.of(page, size, Sort.by("date")));
    }

    public ProductLifting getProductLifting(String id) {
        return productLiftingRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public ProductLifting createProductLifting(ProductLifting bdc) {
        System.out.println(bdc.getWayBill());
        return productLiftingRepo.save(bdc);
    }

    public ProductLifting updateProductLifting(String id, ProductLifting updatedProductLifting) {
        ProductLifting existingProductLifting = getProductLifting(id);
        existingProductLifting.setDate(updatedProductLifting.getDate());
        existingProductLifting.setProduct(updatedProductLifting.getProduct());
        existingProductLifting.setStations(updatedProductLifting.getStations());
        existingProductLifting.setBrv(updatedProductLifting.getBrv());
        existingProductLifting.setBdc(updatedProductLifting.getBdc());
        existingProductLifting.setRate(updatedProductLifting.getRate());
        existingProductLifting.setUppf(updatedProductLifting.getUppf());
        existingProductLifting.setDuty(updatedProductLifting.getDuty());
        existingProductLifting.setBostMargin(updatedProductLifting.getBostMargin());
        existingProductLifting.setNpaMarking(updatedProductLifting.getNpaMarking());
        existingProductLifting.setPriceStabilization(updatedProductLifting.getPriceStabilization());
        existingProductLifting.setPriceDistribution(updatedProductLifting.getPriceDistribution());
        existingProductLifting.setLpgCompensation(updatedProductLifting.getLpgCompensation());
        existingProductLifting.setQty(updatedProductLifting.getQty());
        existingProductLifting.setPrice(updatedProductLifting.getPrice());
        existingProductLifting.setWayBill(updatedProductLifting.getWayBill());
        return productLiftingRepo.save(existingProductLifting);
    }

    public void deleteProductLifting(String id) {
        productLiftingRepo.deleteById(id);
    }

}
