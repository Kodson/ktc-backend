package com.kodsonApp.service;

import com.kodsonApp.domain.Department;
import com.kodsonApp.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(String id) {
        return departmentRepository.findById(id);
    }

    public void deleteById(String id) {
        departmentRepository.deleteById(id);
    }
}
