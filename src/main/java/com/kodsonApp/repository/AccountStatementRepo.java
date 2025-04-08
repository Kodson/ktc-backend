package com.kodsonApp.repository;

import com.kodsonApp.domain.AccountStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountStatementRepo extends JpaRepository<AccountStatement, String> {
    @Override
    Optional<AccountStatement> findById(String id);
    List<AccountStatement> findByStation(String station);
}
