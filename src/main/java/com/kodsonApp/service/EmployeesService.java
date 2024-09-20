package com.kodsonApp.service;

import com.kodsonApp.domain.Employees;
import com.kodsonApp.repository.EmployeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeesService {

    @Autowired
    private EmployeesRepo employeesRepo;

    public Employees saveEmployee(Employees employee) {
        return employeesRepo.save(employee);
    }

    public List<Employees> getAllEmployees() {
        return employeesRepo.findAll();
    }

    public Employees getEmployeeById(String id) {
        return employeesRepo.findById(id).orElse(null);
    }

    public Employees updateEmployee(Employees employee) {
        return employeesRepo.save(employee);
    }

    public void deleteEmployee(String id) {
        employeesRepo.deleteById(id);
    }

    // New Methods
    public List<Employees> getSuspendedEmployees() {
        return employeesRepo.findByStatus("Suspended");
    }

    public List<Employees> getActiveEmployees() {
        return employeesRepo.findByStatus("ACTIVE");
    }

    public List<Employees> getDismissedEmployees() {
        return employeesRepo.findByStatus("Dismissed");
    }

    public Employees reactivateEmployee(String id) {
        Employees employee = employeesRepo.findById(id).orElse(null);
        if (employee != null) {
            employee.setStatus("ACTIVE");
            return employeesRepo.save(employee);
        }
        return null;
    }

//    public List<Employees> getEmployeesByDepartment(String department) {
//        return employeesRepo.findByDepartment(department);
//    }

    public List<Employees> getEmployeesByDepartment(String department) {
        return employeesRepo.findByDepartmentAndStatus(department, "ACTIVE");
    }
}
