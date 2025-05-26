package com.kodsonApp.service;
import com.kodsonApp.domain.Company;
import com.kodsonApp.repository.CompanyRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class CompanyService {

    @Autowired
    private CompanyRepo companyRepository;

    public Company save(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Optional<Company> findById(String id) {
        return companyRepository.findById(id);
    }

    public void deleteById(String id) {
        companyRepository.deleteById(id);
    }
}
