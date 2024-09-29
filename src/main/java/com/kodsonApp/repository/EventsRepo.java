package com.kodsonApp.repository;

import com.kodsonApp.domain.Events;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventsRepo extends JpaRepository<Events, String> {
    // Optional custom query to find events by date
    List<Events> findByDate(LocalDate date);
}
