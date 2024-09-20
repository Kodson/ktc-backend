package com.kodsonApp.service;

import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.repository.PettyCashRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class PettyCashService {
    @Autowired
    private final PettyCashRepo pettyCashRepo;

    public Page<PettyCash> getAllPettyCash(int page, int size) {
        return pettyCashRepo.findAll(PageRequest.of(page, size));
    }

    public PettyCash getPettyCash(String id) {
        return pettyCashRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public PettyCash createPettyCash(PettyCash pettyCash) {
        //pettyCash.setStatus("pending");
        return pettyCashRepo.save(pettyCash);
    }

    public PettyCash updatePettyCash(String id, PettyCash pettyCash) {
        PettyCash existingPettyCash = getPettyCash(id);
        existingPettyCash.setDate(pettyCash.getDate());
        existingPettyCash.setCostCenter(pettyCash.getCostCenter());
        existingPettyCash.setRequestDescription(pettyCash.getRequestDescription());
        existingPettyCash.setStation(pettyCash.getStation());
        existingPettyCash.setAmount(pettyCash.getAmount());
        existingPettyCash.setReceiver(pettyCash.getReceiver());
        existingPettyCash.setStatus(pettyCash.getStatus());
        existingPettyCash.setUserName(pettyCash.getUserName());
        return pettyCashRepo.save(existingPettyCash);
    }

    public void deletePettyCash(String id) {
        PettyCash pettyCash = getPettyCash(id);
        pettyCashRepo.delete(pettyCash);
    }

    public List<PettyCash> getApprovedPettyCash() {
        return pettyCashRepo.findByStatus("approved");
    }

    public List<PettyCash> getPendingPettyCash() {
        return pettyCashRepo.findByStatus("pending");
    }

    public List<PettyCash> getSuspendedPettyCash() {
        return pettyCashRepo.findByStatus("suspended");
    }

    public List<PettyCash> getDeclinedPettyCash() {
        return pettyCashRepo.findByStatus("declined");
    }

    public List<PettyCash> getApprovedPettyCashBetweenDates(LocalDate startDate, LocalDate endDate) {
        return pettyCashRepo.findByStatusAndDateBetween("approved", startDate, endDate);
    }

    public List<PettyCash> getRequestsByStatus(String status) {
        return pettyCashRepo.findByStatus(status);
    }
    public PettyCash updateStatus(String id, PettyCash pettyCash) {
        PettyCash existingPettyCash = getPettyCash(id);
        existingPettyCash.setStatus(pettyCash.getStatus());
        return pettyCashRepo.save(existingPettyCash);
    }

}
