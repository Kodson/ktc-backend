package com.kodsonApp.service;

import com.kodsonApp.domain.CreditCollection;
import com.kodsonApp.repository.CreditCollectionRepo;
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
public class CreditCollectionService {
    @Autowired
    private final CreditCollectionRepo creditCollectionRepo;

    public Page<CreditCollection> getAllCreditCollections(int page, int size) {
        return creditCollectionRepo.findAll(PageRequest.of(page, size));
    }

    public CreditCollection getCreditCollection(String id) {
        return creditCollectionRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public CreditCollection createBdc(CreditCollection bdc) {
        return creditCollectionRepo.save(bdc);
    }

    public void deleteExpense(String id) {
        creditCollectionRepo.deleteById(id);
    }

    public List<CreditCollection> getCreditCollectionByStation(String station){
        return creditCollectionRepo.findByStation(station);
    }
}
