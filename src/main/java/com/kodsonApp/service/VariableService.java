// File: com/kodsonApp/service/VariableService.java

package com.kodsonApp.service;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
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
public class VariableService {

    @Autowired
    private VariablesRepo variablesRepo;
/*
    public List<Variables> findAll() {
        return variablesRepo.findByStatus("Prepared");
    }
*/
// Pagination for findAll
public Page<Variables> findAll(int page, int size, String sortDirection) {
    Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), "date"); // Sort by 'date' field
    Pageable pageable = PageRequest.of(page, size, sort);
    return variablesRepo.findByStatus("Prepared", pageable);
}

    public Optional<Variables> findById(String id) {
        return variablesRepo.findById(id);
    }

    public Variables save(Variables variables) {
        variables.setStatus("Prepared");
        return variablesRepo.save(variables);
    }

    public void deleteById(String id) {
        variablesRepo.deleteById(id);
    }

    // New method to move selected variables to "Moved" status
    public void moveSelectedVariables(List<String> variableIds) {
        List<Variables> variablesList = variablesRepo.findAllById(variableIds);
        for (Variables variable : variablesList) {
            variable.setStatus("Moved");
        }
        variablesRepo.saveAll(variablesList);
    }

   /*
    public List<Variables> findAllMovedVariables() {
        return variablesRepo.findByStatus("Moved");
    }
*/
   // Pagination for moved variables
   public Page<Variables> findAllMovedVariables(Pageable pageable) {
       return variablesRepo.findByStatus("Moved", pageable);
   }
   /*
    public List<Variables> getTripsByDateRange(LocalDate startDate, LocalDate endDate) {
        return variablesRepo.findByDateBetween(startDate, endDate);
    }
*/
   // Pagination for trips by date range
   public Page<Variables> getTripsByDateRange(LocalDate startDate, LocalDate endDate, Pageable pageable) {
       return variablesRepo.findByDateBetween(startDate, endDate, pageable);
   }

    // Method for searching with pagination
    public Page<Variables> searchVariables(String brv, String wayBillNum, String bvo, String month, String subCompany, Pageable pageable) {
        return variablesRepo.findByBrvContainingOrWayBillNumContainingOrBvoContainingOrMonthContainingOrSubCompanyContaining(
                brv, wayBillNum, bvo, month, subCompany, pageable);
    }

}
