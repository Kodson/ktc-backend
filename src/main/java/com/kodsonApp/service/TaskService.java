package com.kodsonApp.service;

import com.kodsonApp.domain.MajorTask;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.domain.Tasks;
import com.kodsonApp.repository.TaskRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepo taskRepo;

    public Page<Tasks> getAllTask(int page, int size) {
        return taskRepo.findAll(PageRequest.of(page, size));
    }

    public Tasks getTask(String id) {
        return taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Tasks createTask(Tasks tasks) {
        return taskRepo.save(tasks);
    }

    public void deletePettyCash(PettyCash pettyCash) {
        // Assignment
    }

}
