package com.kodsonApp.service;

import com.kodsonApp.domain.Bank;
import com.kodsonApp.repository.BankRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class BankService {

    private final BankRepo bankRepo;

    public Page<Bank> getAllBanks(int page, int size) {
        return bankRepo.findAll(PageRequest.of(page, size));
    }

    public Bank getBank(String id) {
        return bankRepo.findById(id).orElseThrow(() -> new RuntimeException("bank not found"));
    }

    public Bank createBank(Bank bank) {
        return bankRepo.save(bank);
    }

    public void deleteExpense(String id) {
        bankRepo.deleteById(id);
    }

    public List<Bank> getBankByStation(String station) {
        return bankRepo.findByStation(station);
    }

}
