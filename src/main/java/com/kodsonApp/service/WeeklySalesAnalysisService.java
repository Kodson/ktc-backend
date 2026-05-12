package com.kodsonApp.service;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.repository.DailySalesRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeeklySalesAnalysisService {

    private final DailySalesRepo dailySalesRepo;

    public Map<String, Object> generateWeeklySalesAnalysis(String stationName, String startDate, String endDate) {
        log.info("Generating weekly sales analysis for station: {} with date range: {} to {}", stationName, startDate, endDate);

        try {
            // Parse the date range for previous month's week 4
            LocalDate previousMonthStartDate = LocalDate.parse(startDate);
            LocalDate previousMonthEndDate = LocalDate.parse(endDate);
            
            // Create previous month's week period from the provided date range
            WeekPeriod previousMonthWeek = new WeekPeriod(4, previousMonthStartDate, previousMonthEndDate);
            
            // Calculate current month's weeks starting from the day after previous month ends
            LocalDate currentMonthStart = previousMonthEndDate.plusDays(1);
            List<WeekPeriod> weekPeriods = calculateWeeksForCurrentMonth(currentMonthStart);
            
            List<Map<String, Object>> weeklyAnalysis = new ArrayList<>();
            
            // First, add the previous month's week as baseline (Week 4 from previous month)
            List<DailySales> previousMonthSales = getDailySalesInRange(stationName, 
                previousMonthWeek.getStartDate().toString(), previousMonthWeek.getEndDate().toString());
            
            Map<String, Object> previousWeekData = calculateWeekMetrics(previousMonthWeek, previousMonthSales, null, 4);
            weeklyAnalysis.add(previousWeekData);
            
            // Then add current month's weeks
            for (int i = 0; i < weekPeriods.size(); i++) {
                WeekPeriod week = weekPeriods.get(i);
                
                // Get sales data for this week
                List<DailySales> weekSales = getDailySalesInRange(stationName, 
                    week.getStartDate().toString(), week.getEndDate().toString());
                
                // Calculate week metrics
                Map<String, Object> weekData = calculateWeekMetrics(week, weekSales, previousWeekData, i + 1);
                weeklyAnalysis.add(weekData);
                
                previousWeekData = weekData;
            }

            Map<String, Object> response = new HashMap<>();
            response.put("stationName", stationName);
            response.put("referenceDate", startDate + " to " + endDate);
            response.put("currentMonth", currentMonthStart.getMonth().toString());
            response.put("weeklyAnalysis", weeklyAnalysis);

            log.info("Weekly sales analysis generated successfully for {} weeks", weekPeriods.size());
            return response;

        } catch (Exception e) {
            log.error("Error generating weekly sales analysis: ", e);
            throw new RuntimeException("Failed to generate weekly sales analysis: " + e.getMessage());
        }
    }

    private List<DailySales> getDailySalesInRange(String stationName, String startDate, String endDate) {
        log.info("Fetching daily sales data for station: {} between {} and {}", stationName, startDate, endDate);

        // Get all daily sales for the station
        Page<DailySales> allSalesPage = dailySalesRepo.findByStation(stationName, PageRequest.of(0, 1000));
        List<DailySales> allSales = allSalesPage.getContent();

        // Filter by date range
        List<DailySales> filteredSales = allSales.stream()
                .filter(sale -> isDateInRange(sale.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        log.info("Found {} daily sales records in date range", filteredSales.size());
        return filteredSales;
    }

    private boolean isDateInRange(String dateStr, String startDate, String endDate) {
        try {
            LocalDate date = LocalDate.parse(dateStr);
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);

            return !date.isBefore(start) && !date.isAfter(end);
        } catch (Exception e) {
            log.warn("Error parsing date: {}", dateStr);
            return false;
        }
    }

    private WeekPeriod calculatePreviousMonthWeek(LocalDate referenceDate) {
        log.info("Calculating previous month week ending on: {}", referenceDate);
        
        // Calculate the start of the previous month's last week (7 days before reference date)
        LocalDate weekStart = referenceDate.minusDays(6);
        LocalDate weekEnd = referenceDate;
        
        log.info("Previous month week: {} to {}", weekStart, weekEnd);
        
        return new WeekPeriod(4, weekStart, weekEnd); // Assume it's week 4 of previous month
    }

    private List<WeekPeriod> calculateWeeksForCurrentMonth(LocalDate currentMonthStart) {
        log.info("Calculating weeks for current month starting from: {}", currentMonthStart);
        
        // Get the target month we're analyzing (the month that currentMonthStart belongs to)
        int targetMonth = currentMonthStart.getMonthValue();
        int targetYear = currentMonthStart.getYear();
        
        List<WeekPeriod> weeks = new ArrayList<>();
        LocalDate weekStart = currentMonthStart;
        int weekNumber = 1;
        
        // Generate 7-day weeks, allowing them to span across months
        while (weekNumber <= 5) { // Maximum 5 weeks to cover a full month
            LocalDate weekEnd = weekStart.plusDays(6);
            
            weeks.add(new WeekPeriod(weekNumber, weekStart, weekEnd));
            log.info("Added week {}: {} to {}", weekNumber, weekStart, weekEnd);
            
            weekStart = weekEnd.plusDays(1);
            weekNumber++;
            
            // Stop if we've gone well beyond the target month
            if (weekStart.getMonthValue() != targetMonth && weekStart.getYear() != targetYear && weekNumber > 2) {
                // Only continue if we're still in reasonable range
                LocalDate monthEnd = LocalDate.of(targetYear, targetMonth, 1).withDayOfMonth(
                    LocalDate.of(targetYear, targetMonth, 1).lengthOfMonth());
                if (weekStart.isAfter(monthEnd.plusDays(7))) {
                    break;
                }
            }
        }
        
        log.info("Generated {} weeks for current month analysis", weeks.size());
        return weeks;
    }

    private Map<String, Object> calculateWeekMetrics(WeekPeriod week, List<DailySales> weekSales, 
                                                   Map<String, Object> previousWeekData, int weekNumber) {
        
        Map<String, Object> weekData = new HashMap<>();
        
        // Basic week info - determine month based on week context
        String monthToDisplay;
        if (weekNumber == 4 && previousWeekData == null) {
            // This is the previous month's baseline week
            monthToDisplay = week.getStartDate().getMonth().toString();
        } else {
            // For current month weeks, determine which month has more days
            LocalDate startDate = week.getStartDate();
            LocalDate endDate = week.getEndDate();
            
            // Count days in each month
            int startMonth = startDate.getMonthValue();
            int endMonth = endDate.getMonthValue();
            
            if (startMonth == endMonth) {
                // Week is entirely within one month
                monthToDisplay = startDate.getMonth().toString();
            } else {
                // Week spans two months - use the month where the week starts
                // But for October weeks, always show OCTOBER even if they start in September
                if (endDate.getMonth().toString().equals("OCTOBER")) {
                    monthToDisplay = "OCTOBER";
                } else {
                    monthToDisplay = startDate.getMonth().toString();
                }
            }
        }
        
        weekData.put("month", monthToDisplay);
        weekData.put("week", "WEEK " + weekNumber);
        
        weekData.put("timePeriod", formatDateRange(week.getStartDate(), week.getEndDate()));
        
        // Check if there's no sales data - set everything to 0
        if (weekSales == null || weekSales.isEmpty()) {
            log.info("No sales data found for week {}, setting all values to 0", weekNumber);
            weekData.put("salesLtrs", 0.0);
            weekData.put("pms", 0.0);
            weekData.put("ago", 0.0);
            weekData.put("diffPms", 0.0);
            weekData.put("diffAgo", 0.0);
            weekData.put("differenceLtrs", 0.0);
            weekData.put("percentageChange", "0.00%");
            return weekData;
        }
        
        // Calculate sales metrics
        double totalSalesLtrs = weekSales.stream()
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();
        
        double pmsSales = weekSales.stream()
                .filter(sale -> "PMS".equalsIgnoreCase(sale.getProduct()))
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();
        
        double agoSales = weekSales.stream()
                .filter(sale -> "AGO".equalsIgnoreCase(sale.getProduct()))
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();
        
        weekData.put("salesLtrs", Math.round(totalSalesLtrs * 100.0) / 100.0);
        weekData.put("pms", Math.round(pmsSales * 100.0) / 100.0);
        weekData.put("ago", Math.round(agoSales * 100.0) / 100.0);
        
        // Calculate differences and percentage changes if previous week data exists
        if (previousWeekData != null) {
            double prevPms = (Double) previousWeekData.get("pms");
            double prevAgo = (Double) previousWeekData.get("ago");
            double prevTotalSales = (Double) previousWeekData.get("salesLtrs");
            
            double diffPms = pmsSales - prevPms;
            double diffAgo = agoSales - prevAgo;
            double diffTotal = totalSalesLtrs - prevTotalSales;
            
            weekData.put("diffPms", Math.round(diffPms * 100.0) / 100.0);
            weekData.put("diffAgo", Math.round(diffAgo * 100.0) / 100.0);
            weekData.put("differenceLtrs", Math.round(diffTotal * 100.0) / 100.0);
            
            // Calculate percentage change
            double percentageChange;
            if (prevTotalSales == 0.0 && totalSalesLtrs > 0.0) {
                // When previous week had 0 sales and current week has sales, show as 0.0%
                percentageChange = 0.0;
            } else if (prevTotalSales != 0) {
                percentageChange = (diffTotal / prevTotalSales) * 100;
            } else {
                percentageChange = 0.0;
            }
            weekData.put("percentageChange", Math.round(percentageChange * 100.0) / 100.0 + "%");
            
        } else {
            // First week (previous month baseline) - no comparison possible
            weekData.put("diffPms", pmsSales);
            weekData.put("diffAgo", agoSales);
            weekData.put("differenceLtrs", totalSalesLtrs);
            weekData.put("percentageChange", "100.00%");
        }
        
        log.info("Week {} metrics: Sales={}, PMS={}, AGO={}", weekNumber, totalSalesLtrs, pmsSales, agoSales);
        
        return weekData;
    }

    private String formatDateRange(LocalDate startDate, LocalDate endDate) {
        // Format like "23rd - 29th" or "30th - 5th"
        String startDay = formatDayWithSuffix(startDate.getDayOfMonth());
        String endDay = formatDayWithSuffix(endDate.getDayOfMonth());
        return startDay + " - " + endDay;
    }

    private String formatDayWithSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return day + "th";
        }
        switch (day % 10) {
            case 1: return day + "st";
            case 2: return day + "nd";
            case 3: return day + "rd";
            default: return day + "th";
        }
    }

    // Inner class to represent a week period
    private static class WeekPeriod {
        private final int weekNumber;
        private final LocalDate startDate;
        private final LocalDate endDate;

        public WeekPeriod(int weekNumber, LocalDate startDate, LocalDate endDate) {
            this.weekNumber = weekNumber;
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public int getWeekNumber() { return weekNumber; }
        public LocalDate getStartDate() { return startDate; }
        public LocalDate getEndDate() { return endDate; }
    }
}