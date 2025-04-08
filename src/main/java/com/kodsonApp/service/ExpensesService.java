package com.kodsonApp.service;

import com.kodsonApp.domain.Expenses;
import com.kodsonApp.repository.ExpensesRepo;
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
public class ExpensesService {
    @Autowired
    private final ExpensesRepo expensesRepo;

    public Page<Expenses> getAllExpenses(int page, int size) {
        return expensesRepo.findAll(PageRequest.of(page, size));
    }

    public Expenses getExpenses(String id) {
        return expensesRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Expenses createExpenses(Expenses bdc) {
        return expensesRepo.save(bdc);
    }

    public void deleteExpense(String id) {
        expensesRepo.deleteById(id);
    }

    public List<Expenses> getExpensesByStation(String station) {
        return expensesRepo.findByStation(station);
    }
}
