package com.kodsonApp.resource;

import com.kodsonApp.domain.PettyCash;
import com.kodsonApp.domain.Tasks;
import com.kodsonApp.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/","kodson/task"})
@RequiredArgsConstructor
public class TasksResource {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks tasks) {
        //System.out.println(bdc.getBdc_Name()+" "+ bdc.getDate());
        //return ResponseEntity.ok().body(bdcService.createBdc(bdc));
        return ResponseEntity.created(URI.create("/kodson/task/taskID")).body(taskService.createTask(tasks));
    }

    @GetMapping
    public List<Tasks> getAllTask() {
        return taskService.getAllTask();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tasks> getTask(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(taskService.getTask(id));
    }
}
