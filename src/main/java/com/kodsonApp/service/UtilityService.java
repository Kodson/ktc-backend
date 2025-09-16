package com.kodsonApp.service;

import com.kodsonApp.domain.Utility;
import com.kodsonApp.repository.UtilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UtilityService {
    @Autowired
    private UtilityRepo utilityRepo;

    public Utility createUtility(Utility utility) {
        // Calculate days overdue if dueDate is provided
        if (utility.getDueDate() != null) {
            LocalDate today = LocalDate.now();
            if (utility.getDueDate().isBefore(today)) {
                long daysBetween = ChronoUnit.DAYS.between(utility.getDueDate(), today);
                utility.setDaysOverdue((int) daysBetween);
            } else {
                utility.setDaysOverdue(0);
            }
        }

        return utilityRepo.save(utility);
    }

    public Page<Utility> getAllUtility(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "dueDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        return utilityRepo.findAll(pageable);
    }

    public Utility getUtilityById(String id) {
        return utilityRepo.findById(id).orElseThrow(() -> new RuntimeException("Utility not found"));
    }

    public void deleteUtility(String id) {
        utilityRepo.deleteById(id);
    }

    public List<Utility> getUtilityByStation(String stationId) {
        return utilityRepo.findByStationId(stationId);
    }

    public List<Utility> getUtilityByStatus(String status) {
        return utilityRepo.findByStatus(status);
    }

    public List<Utility> getUtilityByType(String utilityType) {
        return utilityRepo.findByUtility(utilityType);
    }

    public List<Utility> getUtilityByStationAndStatus(String stationId, String status) {
        return utilityRepo.findByStationIdAndStatus(stationId, status);
    }

    public Utility updateUtility(Utility utilityDetails, String id) {
        Utility utility = utilityRepo.findById(id).orElseThrow(() -> new RuntimeException("Utility not found"));

        utility.setDueDate(utilityDetails.getDueDate());
        utility.setUtility(utilityDetails.getUtility());
        utility.setProvider(utilityDetails.getProvider());
        utility.setBillNumber(utilityDetails.getBillNumber());
        utility.setPeriod(utilityDetails.getPeriod());
        utility.setConsumption(utilityDetails.getConsumption());
        utility.setAmount(utilityDetails.getAmount());
        utility.setStatus(utilityDetails.getStatus());
        utility.setPriority(utilityDetails.getPriority());
        utility.setStationId(utilityDetails.getStationId());
        utility.setStationName(utilityDetails.getStationName());
        utility.setCreatedBy(utilityDetails.getCreatedBy());

        // Recalculate days overdue
        if (utility.getDueDate() != null) {
            LocalDate today = LocalDate.now();
            if (utility.getDueDate().isBefore(today)) {
                long daysBetween = ChronoUnit.DAYS.between(utility.getDueDate(), today);
                utility.setDaysOverdue((int) daysBetween);
            } else {
                utility.setDaysOverdue(0);
            }
        }

        return utilityRepo.save(utility);
    }
}
