package com.kodsonApp.resource;

import com.kodsonApp.domain.Events;
import com.kodsonApp.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/events")
@CrossOrigin(origins = "*") // Allow requests from any origin (for React)
public class EventsResource {

    @Autowired
    private EventsService eventsService;

    @GetMapping
    public List<Events> getAllEvents() {
        return eventsService.getAllEvents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Events> getEventById(@PathVariable String id) {
        return eventsService.getEventById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Events createEvent(@RequestBody Events event) {
        return eventsService.saveEvent(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Events> updateEvent(@PathVariable String id, @RequestBody Events eventDetails) {
        try {
            Events updatedEvent = eventsService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable String id) {
        eventsService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
