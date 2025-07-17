// MajorTaskService.java
package com.kodsonApp.service;

import com.kodsonApp.domain.MajorTask;
import com.kodsonApp.repository.MajorTaskRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class MajorTaskService {
    @Autowired
    private final MajorTaskRepo majorTaskRepo;

    public List<MajorTask> getAllMajorTask() {
        return majorTaskRepo.findAll();
    }

    public MajorTask getMajorTask(String id) {
        return majorTaskRepo.findById(id).orElseThrow(() -> new RuntimeException("Task not found"));
    }

    public MajorTask createMajorTask(MajorTask majorTask) {
        return majorTaskRepo.save(majorTask);
    }

    public void deleteMajorTask(String id) {
        majorTaskRepo.deleteById(id);
    }

    public MajorTask updateTaskMembers(String id, String members) {
        MajorTask task = getMajorTask(id);
        task.setMembers(members);
        return majorTaskRepo.save(task);
    }

    // MajorTaskService.java

    public List<MajorTask> getTasksByUserOrMember(String userName) {
        List<MajorTask> allTasks = majorTaskRepo.findAll();

        return allTasks.stream()
                .filter(task ->
                        userName.equals(task.getUserName()) || // safe null-free check
                                (task.getMembers() != null && task.getMembers().contains(userName))
                )
                .collect(Collectors.toList());
    }


    public MajorTask updateMajorTask(String id, MajorTask updatedTask) {
        MajorTask existingTask = getMajorTask(id);
        existingTask.setTaskName(updatedTask.getTaskName());
        existingTask.setDate(updatedTask.getDate());
        return majorTaskRepo.save(existingTask);
    }


}
