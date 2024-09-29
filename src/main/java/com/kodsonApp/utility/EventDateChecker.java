package com.kodsonApp.utility;

import com.kodsonApp.service.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EventDateChecker {

    @Autowired
    private EventsService eventsService;

    // Runs every day at 9:00 AM to check events happening today
    @Scheduled(cron = "0 0 9 * * ?")
    public void checkEvents() {
        eventsService.checkEventDates();
    }
}
