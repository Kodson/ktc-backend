// File: com/kodsonApp/resource/VariablesResource.java

package com.kodsonApp.resource;
import com.kodsonApp.domain.Shortages;
import com.kodsonApp.domain.Surcharge;
import com.kodsonApp.service.ShortageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api2/shortages")
public class ShortageResource {

    @Autowired
    private ShortageService variableService;

    @GetMapping
    public ResponseEntity<Page<Shortages>> getAllVariables(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection) {


        Page<Shortages> variablesPage = variableService.findAll(page,size,sortDirection);
        return ResponseEntity.ok(variablesPage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Shortages> getVariableById(@PathVariable String id) {
        Optional<Shortages> variables = variableService.findById(id);
        return variables.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Shortages createVariable(@RequestBody Shortages variables) {
        return variableService.save(variables);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Shortages> updateVariable(@PathVariable String id, @RequestBody Shortages variables) {
        if (!variableService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        variables.setId(id);
        return ResponseEntity.ok(variableService.save(variables));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVariable(@PathVariable String id) {
        if (!variableService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        variableService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/fetchReports")
    public ResponseEntity<Page<Shortages>> fetchReports(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, sortDirection.equals("desc") ?
                org.springframework.data.domain.Sort.by("date").descending() :
                org.springframework.data.domain.Sort.by("date").ascending());
        Page<Shortages> reportsPage = variableService.getTripsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(reportsPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<Shortages>> searchVariables(
            @RequestParam(required = false) String brv,
            @RequestParam(required = false) String wayBillNum,
            @RequestParam(required = false) String bvo,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) String subCompany,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        Pageable pageable = PageRequest.of(page, size, sortDirection.equals("desc") ?
                org.springframework.data.domain.Sort.by("date").descending() :
                org.springframework.data.domain.Sort.by("date").ascending());
        Page<Shortages> variablesPage = variableService.searchVariables(brv, wayBillNum, bvo, month, subCompany, pageable);
        return ResponseEntity.ok(variablesPage);
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Shortages>> getPayrollByEmployee(@PathVariable String employeeId) {
        List<Shortages> payrolls = variableService.getPayrollByEmployee(employeeId);
        return ResponseEntity.ok(payrolls);
    }
}
