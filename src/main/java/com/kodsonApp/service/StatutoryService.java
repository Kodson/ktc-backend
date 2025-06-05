package com.kodsonApp.service;
import com.kodsonApp.domain.*;
import com.kodsonApp.repository.StatutoryRepo;
import com.kodsonApp.utility.PettyCashSms;
import org.apache.kafka.common.metrics.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
@Service
public class StatutoryService {
    @Autowired
    private StatutoryRepo statutoryRepo;

    public Statutory createStatutory(Statutory statutory) {
        return statutoryRepo.save(statutory);
    }

    public Page<Statutory> getAllStatutory(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return statutoryRepo.findAll(pageable);
    }

    public Statutory getStatutoryById(String id) {
        return statutoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public void deleteStatutory(String id) {
        statutoryRepo.deleteById(id);
    }

    public List<Statutory> getStatutoryByUser(String station) {
        return statutoryRepo.findByStation(station);
    }

    public Statutory updateStatutory(Statutory statutoryDetails , String id) {
        Statutory statutory = statutoryRepo.findById(id).orElseThrow(() -> new RuntimeException("statutory not found"));
        statutory.setDate(statutoryDetails.getDate());
        statutory.setCertificate(statutoryDetails.getCertificate());
        statutory.setExpiryDate(statutoryDetails.getExpiryDate());
        statutory.setStatus(statutoryDetails.getStatus());
        return statutoryRepo.save(statutory);
    }

    // Runs daily at midnight (00:00)
    @Scheduled(cron = "0 0 10 * * *")
    public void checkTasksDueInOneDay() throws IOException {
        Page<Statutory> allStatutory = getAllStatutory(0, 1000, "desc");
        LocalDate today = LocalDate.now();
        PettyCashSms pettyCashSms = new PettyCashSms();
        for (Statutory statutory : allStatutory) {
            if ("false".equalsIgnoreCase(String.valueOf(statutory.getExpiryDate())) || statutory.getExpiryDate() == null) {
                // Parse task due date
                LocalDate dueDate = LocalDate.parse(String.valueOf(statutory.getExpiryDate()), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (dueDate.minusDays(10).isEqual(today)) {
                    String phone = statutory.getPhone();

                    pettyCashSms.sendTask(phone,statutory.getCertificate());
                }
            }
        }
    }
}
