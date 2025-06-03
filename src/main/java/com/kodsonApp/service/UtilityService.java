package com.kodsonApp.service;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.domain.Loans;
import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Utility;
import com.kodsonApp.repository.LoanRepo;
import com.kodsonApp.repository.UtilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilityService {
    @Autowired
    private UtilityRepo utilityRepo;

    public Utility createUtility(Utility utility) {
        return utilityRepo.save(utility);
    }

    public Page<Utility> getAllUtility(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return utilityRepo.findAll(pageable);
    }

    public Utility getUtilityById(String id) {
        return utilityRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public void deleteUtility(String id) {
        utilityRepo.deleteById(id);
    }

    public List<Utility> getUtilityByUser(String station) {
        return utilityRepo.findByStation(station);
    }

    public Utility updateUtility(Utility utilityDetails , String id) {
        Utility utility = utilityRepo.findById(id).orElseThrow(() -> new RuntimeException("Utility not found"));
        utility.setDate(utilityDetails.getDate());
        utility.setDescription(utilityDetails.getDescription());
        utility.setPrepaid(utilityDetails.getPrepaid());
        utility.setGenset(utilityDetails.getGenset());
        utility.setWater(utilityDetails.getWater());
        return utilityRepo.save(utility);
    }
}
