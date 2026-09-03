package com.kodsonApp.resource;

import com.kodsonApp.domain.Employees;
import com.kodsonApp.service.EmployeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/employees")
public class EmployeesResource {

    @Autowired
    private EmployeesService employeesService;

    @PostMapping
    public Employees saveEmployee(@RequestBody Employees employee) {
        return employeesService.saveEmployee(employee);
    }

    @GetMapping
    public List<Employees> getAllEmployees() {
        return employeesService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Employees getEmployeeById(@PathVariable String id) {
        return employeesService.getEmployeeById(id);
    }

    @PutMapping
    public Employees updateEmployee(@RequestBody Employees employee) {
        return employeesService.updateEmployee(employee);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable String id) {
        employeesService.deleteEmployee(id);
    }

    // New Endpoints
    @GetMapping("/suspended")
    public List<Employees> getSuspendedEmployees() {
        return employeesService.getSuspendedEmployees();
    }

    @GetMapping("/dismissed")
    public List<Employees> getDismissedEmployees() {
        return employeesService.getDismissedEmployees();
    }

    @GetMapping("/active")
    public List<Employees> getActiveEmployees() {
        return employeesService.getActiveEmployees();
    }

    @PutMapping("/reactivate/{id}")
    public Employees reactivateEmployee(@PathVariable String id) {
        return employeesService.reactivateEmployee(id);
    }

    @GetMapping("/department/{department}")
    public List<Employees> getEmployeesByDepartment(@PathVariable String department) {
        return employeesService.getEmployeesByDepartment(department);
    }
}
