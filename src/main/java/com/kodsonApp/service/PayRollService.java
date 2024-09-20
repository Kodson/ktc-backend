package com.kodsonApp.service;

import com.kodsonApp.domain.PayRoll;
import com.kodsonApp.repository.PayRollRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class PayRollService {

    @Autowired
    private PayRollRepo payRollRepo;

    @Autowired
    private PhoneService phoneService;

    public List<PayRoll> getAllPayRolls() {
        return payRollRepo.findAll();
    }

    public Optional<PayRoll> getPayRollById(String id) {
        return payRollRepo.findById(id);
    }

    public PayRoll savePayRoll(PayRoll payRoll) {
        return payRollRepo.save(payRoll);
    }

    public void deletePayRoll(String id) {
        payRollRepo.deleteById(id);
    }

    public List<PayRoll> getLast1000Records() {
        return payRollRepo.findLast1000Records();
    }

    public List<PayRoll> getByDepartmentAndMonth(String department, String month) {
        return payRollRepo.findByDepartmentAndMonth(department, month);
    }

    public List<PayRoll> getByCompanyAndMonth(String companyName, String month) {
        return payRollRepo.findByCompanyAndMonth(companyName, month);
    }

    public List<PayRoll> getByMonth(String month) {
        return payRollRepo.findByMonth(month);
    }

    public List<PayRoll> getPayrollByEmployee(String employeeId) {
        return payRollRepo.findByEmployeeId(employeeId);
    }


    public Optional<PayRoll> getLatestPayrollByEmployeeId(String employeeId) {
        return payRollRepo.findLatestPayrollByEmployeeId(employeeId);
    }

    public void sendPayslip(String employeeName, String payrollId, String phone) throws IOException, IOException {
        phoneService.sendMessage(employeeName, payrollId, phone);
    }
}
