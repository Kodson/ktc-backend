// File: com/kodsonApp/resource/VariablesResource.java

package com.kodsonApp.resource;

import com.kodsonApp.domain.Trips;
import com.kodsonApp.domain.Variables;
import com.kodsonApp.service.VariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/variables")
public class VariablesResource {

    @Autowired
    private VariableService variableService;

    @GetMapping
    public List<Variables> getAllVariables() {
        return variableService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Variables> getVariableById(@PathVariable String id) {
        Optional<Variables> variables = variableService.findById(id);
        return variables.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Variables createVariable(@RequestBody Variables variables) {
        return variableService.save(variables);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Variables> updateVariable(@PathVariable String id, @RequestBody Variables variables) {
        if (!variableService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        variables.setId(id);  // Ensure the ID is set correctly
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

    // New endpoint to move selected variables to "Moved" status
    @PutMapping("/move-to-history")
    public ResponseEntity<Void> moveSelectedVariables(@RequestBody List<String> variableIds) {
        variableService.moveSelectedVariables(variableIds);
        return ResponseEntity.ok().build();
    }

    // New endpoint to get all variables with "Moved" status
    @GetMapping("/history")
    public List<Variables> getMovedVariables() {
        return variableService.findAllMovedVariables();
    }

    @GetMapping("/fetchReports")
    public ResponseEntity<List<Variables>> fetchReports(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Variables> reports = variableService.getTripsByDateRange(startDate, endDate);
        return ResponseEntity.ok(reports);
    }
}
