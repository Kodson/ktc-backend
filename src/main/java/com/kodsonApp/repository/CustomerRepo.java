package com.kodsonApp.repository;
import com.kodsonApp.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepo extends JpaRepository<Customer, String> {
    @Override
    Optional<Customer> findById(String id);
    List<Customer> findByStation(String station);
}
