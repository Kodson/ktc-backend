package com.kodsonApp.repository;

import com.kodsonApp.domain.ProductLifting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface ProductLiftingRepo extends JpaRepository<ProductLifting, String> {
    Optional<ProductLifting> findById(String id);
}
