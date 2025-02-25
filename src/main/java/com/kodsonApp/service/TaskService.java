package com.kodsonApp.service;

import com.kodsonApp.domain.MajorTask;
import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.domain.Tasks;
import com.kodsonApp.domain.Trucks;
import com.kodsonApp.repository.TaskRepo;
import com.kodsonApp.utility.PettyCashSms;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        return taskRepo.findAll(Sort.by(Sort.Order.asc("startDate")));
    }

    public Tasks getTask(String id) {
        return taskRepo.findById(id).orElseThrow(() -> new RuntimeException("Contact not found"));
    }

    public Tasks createTask(Tasks tasks) {
        return taskRepo.save(tasks);
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

    public Tasks updateTasks(String id, Tasks updatedTask) {
        Optional<Tasks> taskOptional = taskRepo.findById(id);
        Tasks task = taskOptional.get();
        task.setFinishDate(updatedTask.getFinishDate());
        task.setStartDate(updatedTask.getStartDate());
        task.setTask(updatedTask.getTask());
        return taskRepo.save(task);
    }

    // Runs daily at midnight (00:00)
    @Scheduled(cron = "0 0 10 * * *")
    public void checkTasksDueInOneDay() throws IOException {
        List<Tasks> allTasks = getAllTask();
        LocalDate today = LocalDate.now();
        PettyCashSms pettyCashSms = new PettyCashSms();
        for (Tasks task : allTasks) {
            if ("false".equalsIgnoreCase(task.getCompleted()) || task.getCompleted() == null) {
                // Parse task due date
                LocalDate dueDate = LocalDate.parse(task.getDueDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (dueDate.minusDays(1).isEqual(today)) {
                    String phone = task.getUserPhone();

                    pettyCashSms.sendTask(phone,task.getTask());
                }
            }
        }
    }

    public void deleteTask(String id) {
        Optional<Tasks> taskOptional = taskRepo.findById(id);
        if (taskOptional.isPresent()) {
            taskRepo.deleteById(id);
        } else {
            throw new RuntimeException("Task not found");
        }
    }


}
