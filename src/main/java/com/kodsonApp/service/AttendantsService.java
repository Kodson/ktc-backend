package com.kodsonApp.service;

import com.kodsonApp.domain.Attendants;
import com.kodsonApp.repository.AttendantsRepo;
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
public class AttendantsService {
    @Autowired
    private final AttendantsRepo attendantsRepo;

    public Page<Attendants> getAllAttendants(int page, int size) {
        return attendantsRepo.findAll(PageRequest.of(page, size));
    }

    public Attendants getBdc(String id) {
        return attendantsRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Attendants createBdc(Attendants attendants) {
        return attendantsRepo.save(attendants);
    }

    public void deleteExpense(String id) {
        attendantsRepo.deleteById(id);
    }

    public List<Attendants> getAttendantByStation(String station) {
        return attendantsRepo.findByStation(station);
    }

}
