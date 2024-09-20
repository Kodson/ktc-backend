package com.kodsonApp.resource;

import com.kodsonApp.domain.PayRoll;
import com.kodsonApp.domain.SmsRequest;
import com.kodsonApp.service.PayRollService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/payroll")
public class PayRollResource {

    @Autowired
    private PayRollService payRollService;

    @GetMapping
    public ResponseEntity<List<PayRoll>> getAllPayRolls() {
        return ResponseEntity.ok(payRollService.getAllPayRolls());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PayRoll> getPayRollById(@PathVariable String id) {
        Optional<PayRoll> payRoll = payRollService.getPayRollById(id);
        return payRoll.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PayRoll> createPayRoll(@RequestBody PayRoll payRoll) {
        return ResponseEntity.ok(payRollService.savePayRoll(payRoll));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PayRoll> updatePayRoll(@PathVariable String id, @RequestBody PayRoll payRoll) {
        if (!payRollService.getPayRollById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        payRoll.setId(id);
        return ResponseEntity.ok(payRollService.savePayRoll(payRoll));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayRoll(@PathVariable String id) {
        if (!payRollService.getPayRollById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        payRollService.deletePayRoll(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/last1000")
    public ResponseEntity<List<PayRoll>> getLast1000Records() {
        return ResponseEntity.ok(payRollService.getLast1000Records());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<PayRoll>> getByDepartmentAndMonth(@RequestParam String department, @RequestParam String month) {
        return ResponseEntity.ok(payRollService.getByDepartmentAndMonth(department, month));
    }

    @GetMapping("/tier1")
    public ResponseEntity<List<PayRoll>> getByCompanyAndMonth(@RequestParam String companyName, @RequestParam String month) {
        return ResponseEntity.ok(payRollService.getByCompanyAndMonth(companyName, month));
    }

    @GetMapping("/debt")
    public ResponseEntity<List<PayRoll>> getByMonth(@RequestParam String month) {
        return ResponseEntity.ok(payRollService.getByMonth(month));
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<PayRoll>> getPayrollByEmployee(@PathVariable String employeeId) {
        List<PayRoll> payrolls = payRollService.getPayrollByEmployee(employeeId);
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/latest/{employeeId}")
    public ResponseEntity<String> getLatestPayrollByEmployeeId(@PathVariable String employeeId) {
        Optional<PayRoll> payroll = payRollService.getLatestPayrollByEmployeeId(employeeId);
        return payroll.map(p -> ResponseEntity.ok(p.getId())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/sendSms")
    public ResponseEntity<Void> sendSms(@RequestBody SmsRequest smsRequest) throws IOException {
        payRollService.sendPayslip(smsRequest.getEmployeeName(), smsRequest.getPayrollId(), smsRequest.getTel());
        return ResponseEntity.ok().build();
    }
}


