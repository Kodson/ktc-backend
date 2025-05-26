// File: com/kodsonApp/service/VariableService.java

package com.kodsonApp.service;

import com.kodsonApp.domain.Surcharge;
import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Shortages;
import com.kodsonApp.domain.Variables;
import com.kodsonApp.repository.ShortageRepo;
import com.kodsonApp.repository.VariablesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShortageService {

    @Autowired
    private ShortageRepo variablesRepo;

// Pagination for findAll
    public Page<Shortages> findAll(int page, int size, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
        Pageable pageable = PageRequest.of(page, size, sort);
        return variablesRepo.findAll(pageable);
    }

    public List<Shortages> getPayrollByEmployee(String employeeId) {
        return variablesRepo.findByEmployeeId(employeeId);
    }

    public Optional<Shortages> findById(String id) {
        return variablesRepo.findById(id);
    }

    public Shortages save(Shortages variables) {
        return variablesRepo.save(variables);
    }

    public void deleteById(String id) {
        variablesRepo.deleteById(id);
    }




    // Pagination for trips by date range
    public Page<Shortages> getTripsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return variablesRepo.findByDateBetween(startDate, endDate, pageable);
    }

    // Method for searching with pagination
    public Page<Shortages> searchVariables(String brv, String wayBillNum, String bvo, String month, String subCompany, Pageable pageable) {
        return variablesRepo.findByBrvContainingOrWayBillNumContainingOrBvoContainingOrMonthContaining(
                brv, wayBillNum, bvo, month, pageable);
    }

}
