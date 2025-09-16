package com.kodsonApp.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Performance monitoring utility for tracking operation times
 */
@Component
public class PerformanceMonitor {

    private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitor.class);
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    public static void startTimer() {
        startTime.set(System.currentTimeMillis());
    }

    public static void stopTimer(String operation) {
        Long start = startTime.get();
        if (start != null) {
            long duration = System.currentTimeMillis() - start;
            if (duration > 1000) { // Log if operation takes more than 1 second
                logger.warn("Slow operation detected: {} took {}ms", operation, duration);
            } else {
                logger.debug("Operation: {} took {}ms", operation, duration);
            }
            startTime.remove();
        }
    }

    public static void logSlowQuery(String query, long duration) {
        if (duration > 500) { // Log queries taking more than 500ms
            logger.warn("Slow query detected: {} took {}ms", query, duration);
        }
    }
}
