package com.kodsonApp.resource;

import com.kodsonApp.service.MonthlySalesAnalysisService;
import com.kodsonApp.service.ReportingService;
import com.kodsonApp.service.WeeklySalesAnalysisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
@Slf4j
public class ReportingResource {

    private final ReportingService reportingService;
    private final WeeklySalesAnalysisService weeklySalesAnalysisService;
    private final MonthlySalesAnalysisService monthlySalesAnalysisService;

    @GetMapping("/station/{stationName}")
    public ResponseEntity<Map<String, Object>> getStationReport(
            @PathVariable String stationName,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> report = reportingService.generateStationReport(stationName, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping("/weekly-analysis/{stationName}")
    public ResponseEntity<Map<String, Object>> getWeeklySalesAnalysis(
            @PathVariable String stationName,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        Map<String, Object> analysis = weeklySalesAnalysisService.generateWeeklySalesAnalysis(stationName, startDate, endDate);
        return ResponseEntity.ok(analysis);
    }

    @GetMapping("/monthly-analysis/{stationName}")
    public ResponseEntity<Map<String, Object>> getMonthlySalesAnalysis(
            @PathVariable String stationName,
            @RequestParam int year) {

        try {
            Map<String, Object> analysis = monthlySalesAnalysisService.generateMonthlySalesAnalysis(stationName, year);
            return ResponseEntity.ok(analysis);
            
        } catch (Exception e) {
            log.error("Error generating monthly sales analysis: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to generate monthly sales analysis");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/yearly-summary/{stationName}")
    public ResponseEntity<Map<String, Object>> getYearlySummary(
            @PathVariable String stationName,
            @RequestParam int year) {

        try {
            Map<String, Object> summary = monthlySalesAnalysisService.getYearSummary(stationName, year);
            return ResponseEntity.ok(summary);
            
        } catch (Exception e) {
            log.error("Error generating yearly summary: {}", e.getMessage(), e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to generate yearly summary");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
