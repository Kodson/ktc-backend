package com.kodsonApp.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api2/health")
@CrossOrigin(origins = "http://localhost:3000")
public class HealthController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> serverDetails = new HashMap<>();
        serverDetails.put("status", "UP");
        serverDetails.put("timestamp", Instant.now().toString());
        serverDetails.put("message", "Server is reachable");
        return ResponseEntity.ok(serverDetails);
    }
}