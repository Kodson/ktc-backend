package com.kodsonApp.service;
import com.kodsonApp.domain.*;
import com.kodsonApp.repository.StatutoryRepo;
import com.kodsonApp.utility.PettyCashSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class StatutoryService {
    @Autowired
    private StatutoryRepo statutoryRepo;

    public Statutory createStatutory(Statutory statutory) {
        // Calculate days remaining if expires date is provided
        if (statutory.getExpiresDate() != null) {
            LocalDate today = LocalDate.now();
            if (statutory.getExpiresDate().isAfter(today)) {
                long daysBetween = ChronoUnit.DAYS.between(today, statutory.getExpiresDate());
                statutory.setDaysRemaining((int) daysBetween);
            } else {
                statutory.setDaysRemaining(0);
            }
        }

        return statutoryRepo.save(statutory);
    }

    public Page<Statutory> getAllStatutory(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "expiresDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        return statutoryRepo.findAll(pageable);
    }

    public Statutory getStatutoryById(String id) {
        return statutoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Statutory not found"));
    }

    public void deleteStatutory(String id) {
        statutoryRepo.deleteById(id);
    }

    public List<Statutory> getStatutoryByStation(String stationId) {
        return statutoryRepo.findByStationId(stationId);
    }

    public Page<Statutory> getStatutoryByStation(String stationId, int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "expiresDate");
        Pageable pageable = PageRequest.of(page, size, sort);
        return statutoryRepo.findByStationId(stationId, pageable);
    }

    public List<Statutory> getStatutoryByType(String type) {
        return statutoryRepo.findByType(type);
    }

    public List<Statutory> getStatutoryByPaymentStatus(String paymentStatus) {
        return statutoryRepo.findByPaymentStatus(paymentStatus);
    }

    public List<Statutory> getStatutoryByStationAndStatus(String stationId, String status) {
        return statutoryRepo.findByStationIdAndPaymentStatus(stationId, status);
    }

    public List<Statutory> getStatutoryExpiringBefore(LocalDate date) {
        return statutoryRepo.findByExpiresDateBefore(date);
    }

    public List<Statutory> getStatutoryExpiringBetween(LocalDate startDate, LocalDate endDate) {
        return statutoryRepo.findByExpiresDateBetween(startDate, endDate);
    }

    public Statutory updateStatutory(Statutory statutoryDetails, String id) {
        Statutory statutory = statutoryRepo.findById(id).orElseThrow(() -> new RuntimeException("Statutory not found"));

        statutory.setType(statutoryDetails.getType());
        statutory.setTitle(statutoryDetails.getTitle());
        statutory.setAuthority(statutoryDetails.getAuthority());
        statutory.setReference(statutoryDetails.getReference());
        statutory.setRegisteredDate(statutoryDetails.getRegisteredDate());
        statutory.setIssuedDate(statutoryDetails.getIssuedDate());
        statutory.setExpiresDate(statutoryDetails.getExpiresDate());
        statutory.setFees(statutoryDetails.getFees());
        statutory.setPaymentStatus(statutoryDetails.getPaymentStatus());

        statutory.setAssignee(statutoryDetails.getAssignee());
        statutory.setStationId(statutoryDetails.getStationId());
        statutory.setStationName(statutoryDetails.getStationName());
        statutory.setCreatedBy(statutoryDetails.getCreatedBy());

        // Recalculate days remaining
        if (statutory.getExpiresDate() != null) {
            LocalDate today = LocalDate.now();
            if (statutory.getExpiresDate().isAfter(today)) {
                long daysBetween = ChronoUnit.DAYS.between(today, statutory.getExpiresDate());
                statutory.setDaysRemaining((int) daysBetween);
            } else {
                statutory.setDaysRemaining(0);
            }
        }

        return statutoryRepo.save(statutory);
    }

    // Runs daily at 10 AM to check for expiring statutory documents
    @Scheduled(cron = "0 0 10 * * *")
    public void checkStatutoryExpiringInOneDay() throws IOException {
        LocalDate today = LocalDate.now();
        LocalDate tenDaysFromNow = today.plusDays(10);
        List<Statutory> expiringStatutory = statutoryRepo.findByExpiresDateBetween(today, tenDaysFromNow);

        PettyCashSms pettyCashSms = new PettyCashSms();
        for (Statutory statutory : expiringStatutory) {
            if (statutory.getDaysRemaining() != null && statutory.getDaysRemaining() <= 10) {
                // Send notification for expiring statutory documents
                // You may need to add phone field or get it from station/assignee
                String message = String.format("Statutory document '%s' expires in %d days. Reference: %s",
                    statutory.getTitle(), statutory.getDaysRemaining(), statutory.getReference());

                // Assuming you have a way to get phone number from assignee or station
                // pettyCashSms.sendTask(phoneNumber, message);
            }
        }
    }
}
