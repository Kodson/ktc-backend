package com.kodsonApp.service;

import com.kodsonApp.domain.Events;
import com.kodsonApp.repository.EventsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EventsService {

    @Autowired
    private EventsRepo eventsRepo;

    public List<Events> getAllEvents() {
        return eventsRepo.findAll();
    }

    public Optional<Events> getEventById(String id) {
        return eventsRepo.findById(id);
    }

    public Events saveEvent(Events event) {
        return eventsRepo.save(event);
    }

    public Events updateEvent(String id, Events eventDetails) {
        return eventsRepo.findById(id)
                .map(event -> {
                    event.setDate(eventDetails.getDate());
                    event.setTitle(eventDetails.getTitle());
                    event.setDescription(eventDetails.getDescription());
                    event.setColor(eventDetails.getColor());
                    return eventsRepo.save(event);
                })
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public void deleteEvent(String id) {
        eventsRepo.deleteById(id);
    }

    // Scheduler task to print if event date is up
    public void checkEventDates() {
        LocalDate today = LocalDate.now();
        List<Events> todayEvents = eventsRepo.findByDate(today);

        for (Events event : todayEvents) {
            System.out.println("Reminder: Event '" + event.getTitle() + "' is happening today!");
        }
    }
}
