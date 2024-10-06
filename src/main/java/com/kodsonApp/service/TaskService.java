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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class TaskService {
    @Autowired
    private final TaskRepo taskRepo;

    public List<Tasks> getAllTask() {
        return taskRepo.findAll();
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

    // New method to get tasks by user name
    public List<Tasks> getTasksByUserName(String userName) {
        return taskRepo.findByUserName(userName);
    }

    // Update task completion details
    public Tasks updateTaskCompletion(String id, Tasks updatedTask) throws Exception {
        Optional<Tasks> taskOptional = taskRepo.findById(id);

        if (taskOptional.isPresent()) {
            Tasks task = taskOptional.get();
            task.setFinishDate(updatedTask.getFinishDate());
            task.setPercentage(updatedTask.getPercentage());
            task.setCompleted("true"); // Assuming you have a 'completed' flag
            return taskRepo.save(task);
        } else {
            throw new Exception("Task not found");
        }
    }

}
