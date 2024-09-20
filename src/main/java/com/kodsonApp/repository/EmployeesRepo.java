package com.kodsonApp.repository;

import com.kodsonApp.domain.Employees;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeesRepo extends JpaRepository<Employees, String> {
    List<Employees> findByStatus(String status);
    //List<Employees> findByDepartment(String department);
    List<Employees> findByDepartmentAndStatus(String department, String status);
}
