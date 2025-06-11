package com.kodsonApp.service;
import com.kodsonApp.domain.Utility;
import com.kodsonApp.domain.WashingBay;
import com.kodsonApp.repository.UtilityRepo;
import com.kodsonApp.repository.WashingBayRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WashinBayService {
    @Autowired
    private WashingBayRepo washingBayRepo;

    public WashingBay createWashingBay(WashingBay washingBay) {
        return washingBayRepo.save(washingBay);
    }

    public Page<WashingBay> getAllWashingBay(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return washingBayRepo.findAll(pageable);
    }

    public WashingBay getWashingBayById(String id) {
        return washingBayRepo.findById(id).orElseThrow(() -> new RuntimeException("data not found"));
    }

    public void deleteWashingBay(String id) {
        washingBayRepo.deleteById(id);
    }

    public List<WashingBay> getWashingBayByUser(String station) {
        return washingBayRepo.findByStation(station);
    }

    public WashingBay updateWashingBay(WashingBay washingBayDetails , String id) {
        WashingBay washingBay = washingBayRepo.findById(id).orElseThrow(() -> new RuntimeException("Washing Bay not found"));
        washingBay.setDate(washingBayDetails.getDate());
        washingBay.setVehicles(washingBayDetails.getVehicles());
        washingBay.setTotalSales(washingBayDetails.getTotalSales());
        washingBay.setExpenses(washingBayDetails.getExpenses());
        washingBay.setBank(washingBayDetails.getBank());
        washingBay.setKodson(washingBayDetails.getKodson());
        washingBay.setElectrition(washingBayDetails.getElectrition());
        washingBay.setOperations(washingBayDetails.getOperations());
        return washingBayRepo.save(washingBay);
    }
}
