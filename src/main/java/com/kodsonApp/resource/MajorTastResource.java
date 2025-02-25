package com.kodsonApp.resource;

import com.kodsonApp.domain.MajorTask;
import com.kodsonApp.service.MajorTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = { "/", "api/major" })
@RequiredArgsConstructor
public class MajorTastResource {
    private final MajorTaskService majorTaskService;

    @PostMapping
    public ResponseEntity<MajorTask> createMajorTask(@RequestBody MajorTask majorTask) {
        return ResponseEntity.created(URI.create("/api/major/" + majorTask.getId())).body(majorTaskService.createMajorTask(majorTask));
    }

    @GetMapping
    public List<MajorTask> getAllMajor(){
        return majorTaskService.getAllMajorTask();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MajorTask> getMajor(@PathVariable(value = "id") String id) {
        return ResponseEntity.ok().body(majorTaskService.getMajorTask(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMajor(@PathVariable(value = "id") String id) {
        majorTaskService.deleteMajorTask(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/members")
    public ResponseEntity<MajorTask> updateTaskMembers(@PathVariable(value = "id") String id, @RequestBody MajorTask majorTask) {
        String members = majorTask.getMembers();
        String processedMembers = processMembers(members);
        majorTask.setMembers(processedMembers);
        return ResponseEntity.ok().body(majorTaskService.updateTaskMembers(id, processedMembers));
    }

    private String processMembers(String members) {
        // Process the members string to include only "Peterson, Vincent"
        String[] membersArray = members.split(",");
        for (int i = 0; i < membersArray.length; i++) {
            membersArray[i] = membersArray[i].trim();
        }
        return String.join(", ", membersArray);
    }

    // MajorTaskResource.java

    @GetMapping("/user/{username}")
    public ResponseEntity<List<MajorTask>> getAllTasksForUser(@PathVariable("username") String username) {
        List<MajorTask> tasks = majorTaskService.getTasksByUserOrMember(username);
        return ResponseEntity.ok().body(tasks);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MajorTask> updateMajorTask(@PathVariable String id, @RequestBody MajorTask updatedTask) {
        return ResponseEntity.ok().body(majorTaskService.updateMajorTask(id, updatedTask));
    }


}
