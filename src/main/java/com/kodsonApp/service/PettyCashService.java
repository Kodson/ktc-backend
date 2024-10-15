package com.kodsonApp.service;

import com.kodsonApp.domain.Kodson;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.repository.KodsonRepository;
import com.kodsonApp.repository.PettyCashRepo;
import com.kodsonApp.utility.PettyCashSms;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class PettyCashService {
    @Autowired
    private final PettyCashRepo pettyCashRepo;
    @Autowired
    private KodsonRepository repository;

    PettyCashSms pettyCashSms = new PettyCashSms();

    public Page<PettyCash> getAllPettyCash(int page, int size) {
        return pettyCashRepo.findAll(PageRequest.of(page, size));
    }

    public PettyCash getPettyCash(String id) {
        return pettyCashRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public PettyCash createPettyCash(PettyCash pettyCash) throws IOException {
        //pettyCash.setStatus("pending");
        double amount = pettyCash.getAmount();
        String sender = pettyCash.getUserName();
        pettyCashSms.sendGm(amount,sender);
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

    public List<PettyCash> getApprovedPettyCashWithUser(String userName) {
        return pettyCashRepo.findByStatusAndUserName("approved", userName); // Update to use userName
    }

    public List<PettyCash> getApprovedPettyCashWithStation(String station) {
        return pettyCashRepo.findByStatusAndStation("approved", station); // Update to use userName
    }


    public List<PettyCash> getPendingPettyCash() {
        return pettyCashRepo.findByStatus("pending");
    }

    public List<PettyCash> getPendingPettyCashWithUser(String userName) {
        return pettyCashRepo.findByStatusAndUserName("pending", userName); // Update to use userName
    }

    public List<PettyCash> getPendingPettyCashWithStation(String station) {
        return pettyCashRepo.findByStatusAndStation("pending", station); // Update to use userName
    }

    public List<PettyCash> getSuspendedPettyCash() {
        return pettyCashRepo.findByStatus("suspended");
    }
    public List<PettyCash> getSuspendPettyCashWithUser(String userName) {
        return pettyCashRepo.findByStatusAndUserName("suspended", userName); // Update to use userName
    }

    public List<PettyCash> getSuspendPettyCashWithStation(String station) {
        return pettyCashRepo.findByStatusAndStation("suspended", station); // Update to use userName
    }

    public List<PettyCash> getDeclinedPettyCash() {
        return pettyCashRepo.findByStatus("declined");
    }

    public List<PettyCash> getDeclinedPettyCashWithUser(String userName) {
        return pettyCashRepo.findByStatusAndUserName("declined", userName); // Update to use userName
    }

    public List<PettyCash> getDeclinedPettyCashWithStation(String station) {
        return pettyCashRepo.findByStatusAndStation("declined", station); // Update to use userName
    }

    public List<PettyCash> getApprovedPettyCashBetweenDates(LocalDate startDate, LocalDate endDate, String station) {
        return pettyCashRepo.findByStatusAndDateBetweenAndStation("approved", startDate, endDate, station);
    }

    public List<PettyCash> getRequestsByStatus(String status) {
        return pettyCashRepo.findByStatus(status);
    }


    public PettyCash updateStatus(String id, PettyCash pettyCash) throws IOException {
        PettyCash existingPettyCash = getPettyCash(id);
        existingPettyCash.setStatus(pettyCash.getStatus());
        String senderPhone = existingPettyCash.getUserPhone();
        double amount = existingPettyCash.getAmount();
        String source = existingPettyCash.getStation();
        String stationPhone  = getUserPhoneByUsername(source);
        String description = existingPettyCash.getRequestDescription();
        pettyCashSms.sendManager(amount,senderPhone);
        pettyCashSms.sendStation(amount,description,stationPhone);
        return pettyCashRepo.save(existingPettyCash);
    }


    // New method to get user phone by username
    public String getUserPhoneByUsername(String username) {
        Kodson user = repository.findUserByUsername(username);
        if (user != null) {
            return user.getPhone();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }


}
