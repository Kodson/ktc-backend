package com.kodsonApp.repository;

import com.kodsonApp.domain.CreditCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditCollectionRepo extends JpaRepository<CreditCollection,String> {
    Optional<CreditCollection> findById(String id);
    List<CreditCollection> findByStation(String station);
}
