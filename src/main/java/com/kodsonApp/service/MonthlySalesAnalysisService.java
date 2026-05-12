package com.kodsonApp.service;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.repository.DailySalesRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MonthlySalesAnalysisService {

    private final DailySalesRepo dailySalesRepo;

    @Cacheable(value = "monthlySales", key = "'monthly-analysis-' + #stationName + '-' + #year")
    public Map<String, Object> generateMonthlySalesAnalysis(String stationName, int year) {
        log.info("Generating monthly sales analysis for station: {} and year: {}", stationName, year);

        try {
            Map<String, Object> response = new HashMap<>();
            response.put("stationName", stationName);
            response.put("year", year);
            response.put("title", "MONTHLY SALES IN LITERS (" + year + ")");

            List<Map<String, Object>> monthlyAnalysis = new ArrayList<>();

            // Get previous year's December data for baseline
            Map<String, Object> previousDecember = calculateMonthlyMetrics(stationName, year - 1, 12, null);
            if (previousDecember != null) {
                previousDecember.put("month", "DE_" + String.valueOf(year - 1).substring(2));
                monthlyAnalysis.add(previousDecember);
            }

            // Calculate monthly data for current year
            Map<String, Object> previousMonth = previousDecember;
            String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", 
                                  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

            for (int month = 1; month <= 12; month++) {
                Map<String, Object> monthData = calculateMonthlyMetrics(stationName, year, month, previousMonth);
                if (monthData != null) {
                    monthData.put("month", monthNames[month - 1]);
                    monthlyAnalysis.add(monthData);
                    previousMonth = monthData;
                }
            }

            response.put("monthlyAnalysis", monthlyAnalysis);
            response.put("generatedAt", LocalDate.now().toString());

            log.info("Successfully generated monthly analysis with {} months", monthlyAnalysis.size());
            return response;

        } catch (Exception e) {
            log.error("Error generating monthly sales analysis: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to generate monthly sales analysis", e);
        }
    }

    private Map<String, Object> calculateMonthlyMetrics(String stationName, int year, int month, 
                                                      Map<String, Object> previousMonth) {
        
        // Get start and end dates for the month
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        // Get daily sales data for the month
        List<DailySales> monthlySales = getMonthlySalesData(stationName, startDate, endDate);

        if (monthlySales.isEmpty()) {
            log.info("No sales data found for {}-{:02d}", year, month);
            return null;
        }

        Map<String, Object> monthData = new HashMap<>();

        // Calculate PMS sales
        double pmsSales = monthlySales.stream()
                .filter(sale -> "PMS".equalsIgnoreCase(sale.getProduct()))
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();

        // Calculate AGO sales
        double agoSales = monthlySales.stream()
                .filter(sale -> "AGO".equalsIgnoreCase(sale.getProduct()))
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();

        // Calculate total sales (PMS + AGO)
        double totalSales = pmsSales + agoSales;

        // Round to 2 decimal places
        monthData.put("pmsAgo", Math.round(totalSales * 100.0) / 100.0);
        monthData.put("pms", Math.round(pmsSales * 100.0) / 100.0);
        monthData.put("ago", Math.round(agoSales * 100.0) / 100.0);

        // Calculate differences if previous month data exists
        if (previousMonth != null) {
            double prevTotal = (Double) previousMonth.get("pmsAgo");
            double prevPms = (Double) previousMonth.get("pms");
            double prevAgo = (Double) previousMonth.get("ago");

            double diffTotal = totalSales - prevTotal;
            double diffPms = pmsSales - prevPms;
            double diffAgo = agoSales - prevAgo;

            monthData.put("diffPmsAgo", Math.round(diffTotal * 100.0) / 100.0);
            monthData.put("diffPms", Math.round(diffPms * 100.0) / 100.0);
            monthData.put("diffAgo", Math.round(diffAgo * 100.0) / 100.0);
        } else {
            // First month - no comparison, show totals as differences
            monthData.put("diffPmsAgo", Math.round(totalSales * 100.0) / 100.0);
            monthData.put("diffPms", Math.round(pmsSales * 100.0) / 100.0);
            monthData.put("diffAgo", Math.round(agoSales * 100.0) / 100.0);
        }

        log.info("Month {}-{:02d} metrics: Total={}, PMS={}, AGO={}", 
                year, month, totalSales, pmsSales, agoSales);

        return monthData;
    }

    private List<DailySales> getMonthlySalesData(String stationName, LocalDate startDate, LocalDate endDate) {
        log.info("Fetching monthly sales data for station: {} from {} to {}", 
                stationName, startDate, endDate);

        // Get all daily sales for the station
        List<DailySales> allSales = dailySalesRepo.findByStation(stationName, 
                org.springframework.data.domain.PageRequest.of(0, 10000)).getContent();

        // Filter by date range
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        List<DailySales> filteredSales = allSales.stream()
                .filter(sale -> {
                    try {
                        LocalDate saleDate = LocalDate.parse(sale.getDate(), formatter);
                        return !saleDate.isBefore(startDate) && !saleDate.isAfter(endDate);
                    } catch (Exception e) {
                        log.warn("Error parsing date: {}", sale.getDate());
                        return false;
                    }
                })
                .sorted(Comparator.comparing(sale -> {
                    try {
                        return LocalDate.parse(sale.getDate(), formatter);
                    } catch (Exception e) {
                        return LocalDate.MIN;
                    }
                }))
                .collect(Collectors.toList());

        log.info("Found {} daily sales records in date range", filteredSales.size());
        return filteredSales;
    }

    @Cacheable(value = "monthlySales", key = "'year-summary-' + #stationName + '-' + #year")
    public Map<String, Object> getYearSummary(String stationName, int year) {
        log.info("Generating year summary for station: {} and year: {}", stationName, year);

        Map<String, Object> yearlyAnalysis = generateMonthlySalesAnalysis(stationName, year);
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> monthlyData = (List<Map<String, Object>>) yearlyAnalysis.get("monthlyAnalysis");

        if (monthlyData == null || monthlyData.isEmpty()) {
            return Map.of("error", "No data available for year " + year);
        }

        // Calculate yearly totals (excluding baseline December from previous year)
        double totalPmsAgo = 0.0;
        double totalPms = 0.0;
        double totalAgo = 0.0;
        int monthsWithData = 0;

        for (Map<String, Object> month : monthlyData) {
            String monthName = (String) month.get("month");
            if (!monthName.startsWith("DE_")) { // Skip previous year December
                totalPmsAgo += (Double) month.get("pmsAgo");
                totalPms += (Double) month.get("pms");
                totalAgo += (Double) month.get("ago");
                monthsWithData++;
            }
        }

        Map<String, Object> summary = new HashMap<>();
        summary.put("stationName", stationName);
        summary.put("year", year);
        summary.put("monthsWithData", monthsWithData);
        summary.put("totalPmsAgo", Math.round(totalPmsAgo * 100.0) / 100.0);
        summary.put("totalPms", Math.round(totalPms * 100.0) / 100.0);
        summary.put("totalAgo", Math.round(totalAgo * 100.0) / 100.0);
        summary.put("averageMonthlyPmsAgo", monthsWithData > 0 ? 
                Math.round((totalPmsAgo / monthsWithData) * 100.0) / 100.0 : 0.0);

        return summary;
    }
}