package com.kodsonApp.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.kodsonApp.domain.Kodson;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.repository.KodsonRepository;
import com.kodsonApp.repository.PettyCashRepo;
import com.kodsonApp.utility.PettyCashSms;
import com.kodsonApp.utility.PettySocketHandler;
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

    @Autowired
    private PettySocketHandler customHandler;

    PettyCashSms pettyCashSms = new PettyCashSms();

    // Update the getAllPettyCash method to include sorting by date
    public Page<PettyCash> getAllPettyCash(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findAll(pageable);
    }

    public PettyCash getPettyCash(String id) {
        return pettyCashRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    @Transactional
    public PettyCash createPettyCash(PettyCash pettyCash) throws IOException {
        try {
            pettyCashRepo.save(pettyCash);
            pettyCashSms.sendGm(pettyCash.getAmount(), pettyCash.getUserName());
            customHandler.broadcastMessage("New petty cash request submitted.");
            return pettyCash;
        } catch (Exception e) {
            System.err.println("Error creating petty cash request: " + e.getMessage());
            throw e;
        }
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

    public Page<PettyCash> getApprovedPettyCashWithUser(String userName, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndUserName("approved", userName, pageable);
    }



    public Page<PettyCash> getApprovedPettyCashWithStation(String station, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndStation("approved", station, pageable);
    }


    public List<PettyCash> getPendingPettyCash() {
        return pettyCashRepo.findByStatus("pending");
    }



    public Page<PettyCash> getPendingPettyCashWithUser(String userName, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndUserName("pending", userName, pageable);
    }



    public Page<PettyCash> getPendingPettyCashWithStation(String station, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndStation("pending", station, pageable);
    }

    public List<PettyCash> getSuspendedPettyCash() {
        return pettyCashRepo.findByStatus("suspended");
    }


    public Page<PettyCash> getSuspendPettyCashWithUser(String userName, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndUserName("suspended", userName, pageable);
    }


    public Page<PettyCash> getSuspendPettyCashWithStation(String station, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndStation("suspended", station, pageable);
    }

    public List<PettyCash> getDeclinedPettyCash() {
        return pettyCashRepo.findByStatus("declined");
    }


    public Page<PettyCash> getDeclinedPettyCashWithUser(String userName, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndUserName("declined", userName, pageable);
    }



    public Page<PettyCash> getDeclinedPettyCashWithStation(String station, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndStation("declined", station, pageable);
    }

    public List<PettyCash> getApprovedPettyCashBetweenDates(LocalDate startDate, LocalDate endDate, String station
            , int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByStatusAndDateBetweenAndStation("approved", startDate, endDate, station, pageable);
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

    public Page<PettyCash> searchPettyCash(String costCenter,String receiver, String status, String description, String station, String userName, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sorting by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return pettyCashRepo.findByCostCenterContainingOrReceiverContainingOrStatusContainingOrRequestDescriptionContainingOrStationContainingOrUserNameContaining(costCenter,receiver,status, description, station, userName, pageable);
    }

}
