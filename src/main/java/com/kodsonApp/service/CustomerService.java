package com.kodsonApp.service;

import com.kodsonApp.domain.Customer;
import com.kodsonApp.repository.CustomerRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class CustomerService {
    @Autowired
    private final CustomerRepo customerRepo;

    public Page<Customer> getAllCustomers(int page, int size) {
        return customerRepo.findAll(PageRequest.of(page, size));
    }

    public Customer getCustomer(String id) {
        return customerRepo.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepo.save(customer);
    }

    public void deleteExpense(String id) {
        customerRepo.deleteById(id);
    }

    public List<Customer> getCustomerByStation(String station) {
        return customerRepo.findByStation(station);
    }

}
