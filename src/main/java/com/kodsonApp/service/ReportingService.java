package com.kodsonApp.service;

import com.kodsonApp.domain.DailySales;
import com.kodsonApp.domain.Supply;
import com.kodsonApp.repository.DailySalesRepo;
import com.kodsonApp.repository.SupplyRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportingService {

    private final DailySalesRepo dailySalesRepo;
    private final SupplyRepo supplyRepo;

    public Map<String, Object> generateStationReport(String stationName, String startDate, String endDate) {
        log.info("Generating station report for: {}, from: {} to: {}", stationName, startDate, endDate);

        try {
            // Get data from database
            List<DailySales> dailySalesData = getDailySalesInRange(stationName, startDate, endDate);
            List<Supply> supplyData = getSupplyInRange(stationName, startDate, endDate);

            log.info("Found {} daily sales records and {} supply records", dailySalesData.size(), supplyData.size());

            // Generate report structure
            Map<String, Object> report = new HashMap<>();
            Map<String, Object> totals = new HashMap<>();

            // Generate PMS data with synchronized sales unit price
            Map<String, Object> pmsData = generateProductDataWithSynchronization(dailySalesData, supplyData, "PMS");
            totals.put("pms", pmsData);

            // Generate AGO data with synchronized sales unit price
            Map<String, Object> agoData = generateProductDataWithSynchronization(dailySalesData, supplyData, "AGO");
            totals.put("ago", agoData);

            // Generate rate data
            Map<String, Object> pmsRate = generateRateData(dailySalesData, supplyData, "PMS");
            totals.put("pms_rate", pmsRate);

            Map<String, Object> agoRate = generateRateData(dailySalesData, supplyData, "AGO");
            totals.put("ago_rate", agoRate);

            // Generate value data
            Map<String, Object> pmsValue = generateValueData(dailySalesData, "PMS", pmsRate);
            totals.put("pms_value", pmsValue);

            Map<String, Object> agoValue = generateValueData(dailySalesData, "AGO", agoRate);
            totals.put("ago_value", agoValue);

            // Generate total data with synchronized columns
            Map<String, Object> totalData = generateTotalDataWithSynchronizedColumns(pmsData, agoData, dailySalesData);
            totals.put("total", totalData);

            // Generate total values data (sum of PMS and AGO value rows)
            Map<String, Object> totalValuesData = generateTotalValuesData(pmsValue, agoValue);
            totals.put("totalValues", totalValuesData);

            report.put("totals", totals);

            // Generate summary calculations
            Map<String, Object> summary = generateSummary(dailySalesData, supplyData, totalValuesData);
            report.put("summary", summary);

            log.info("Successfully generated station report for: {}", stationName);
            return report;

        } catch (Exception e) {
            log.error("Error generating station report: ", e);
            throw new RuntimeException("Failed to generate station report: " + e.getMessage());
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

    private List<Supply> getSupplyInRange(String stationName, String startDate, String endDate) {
        log.info("Fetching supply data for station: {} between {} and {}", stationName, startDate, endDate);

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            List<Supply> pmsSupply = supplyRepo.findByStationAndProductAndDateBetween(stationName, "PMS", start, end);
            List<Supply> agoSupply = supplyRepo.findByStationAndProductAndDateBetween(stationName, "AGO", start, end);

            List<Supply> allSupply = new ArrayList<>();
            allSupply.addAll(pmsSupply != null ? pmsSupply : Collections.emptyList());
            allSupply.addAll(agoSupply != null ? agoSupply : Collections.emptyList());

            log.info("Found {} supply records in date range", allSupply.size());
            return allSupply;
        } catch (ParseException e) {
            log.error("Error parsing dates for supply query", e);
            return Collections.emptyList();
        }
    }

    private Map<String, Object> generateProductData(List<DailySales> dailySalesData, List<Supply> supplyData, String product) {
        log.info("Generating {} product data", product);

        Map<String, Object> productData = new HashMap<>();

        // Filter data for specific product
        List<DailySales> productSales = dailySalesData.stream()
                .filter(sale -> product.equalsIgnoreCase(sale.getProduct()))
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        List<Supply> productSupply = supplyData.stream()
                .filter(supply -> product.equalsIgnoreCase(supply.getProduct()))
                .sorted(Comparator.comparing(Supply::getDate))
                .collect(Collectors.toList());

        // Opening stock (from the first day's opening stock)
        double openingStock = productSales.isEmpty() ? 0.0 :
                (productSales.get(0).getOpenSL() != null ? productSales.get(0).getOpenSL() : 0.0);

        // Total supply between dates
        double totalSupply = productSupply.stream()
                .mapToDouble(supply -> supply.getQty() != null ? supply.getQty() : 0.0)
                .sum();

        // Available stock
        double availableStock = openingStock + totalSupply;

        // Sales cost (sum of all salesL)
        double salesCost = productSales.stream()
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();

        // Sales unit price (using synchronized approach)
        Object salesUnitPrice = generateSynchronizedSalesUnitPrice(dailySalesData, product);

        // Closing stock
        double closingStock = availableStock - salesCost;

        // Closing dispensing (closingSL of the last date)
        double closingDispensing = productSales.isEmpty() ? 0.0 :
                productSales.stream()
                        .max(Comparator.comparing(DailySales::getDate))
                        .map(sale -> sale.getClosingSL() != null ? sale.getClosingSL() : 0.0)
                        .orElse(0.0);

        // Underground gains
        double undergroundGains = closingDispensing - closingStock;

        // Pump gains (salesCost * 0.0025)
        double pumpGains = salesCost * 0.0025;

        productData.put("openingStock", Math.round(openingStock * 100.0) / 100.0);
        productData.put("supply", Math.round(totalSupply * 100.0) / 100.0);
        productData.put("availableStock", Math.round(availableStock * 100.0) / 100.0);
        productData.put("salesCost", Math.round(salesCost * 100.0) / 100.0);
        productData.put("salesUnitPrice", salesUnitPrice);
        productData.put("closingStock", Math.round(closingStock * 100.0) / 100.0);
        productData.put("closingDispensing", Math.round(closingDispensing * 100.0) / 100.0);
        productData.put("undergroundGains", Math.round(undergroundGains * 100.0) / 100.0);
        productData.put("pumpGains", Math.round(pumpGains * 100.0) / 100.0);

        log.info("{} data generated - Opening: {}, Supply: {}, Sales: {}",
                product, openingStock, totalSupply, salesCost);

        return productData;
    }

    private Object generateSalesUnitPrice(List<DailySales> productSales) {
        if (productSales.isEmpty()) {
            return Map.of("totalSales", 0.0);
        }

        // Sort sales by date to handle chronological price changes
        List<DailySales> sortedSales = productSales.stream()
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        // Track price changes and group by date ranges
        List<Map<String, Object>> priceRanges = new ArrayList<>();
        Double lastRate = null;
        String rangeStartDate = null;
        String rangeEndDate = null;
        double currentRangeTotal = 0.0;

        for (DailySales sale : sortedSales) {
            Double rate = sale.getRate();
            Double salesL = sale.getSalesL();
            String currentDate = sale.getDate();

            if (rate != null && salesL != null) {
                // If rate changed, finalize current range and start new one
                if (lastRate != null && !rate.equals(lastRate)) {
                    // Finalize current range
                    if (rangeStartDate != null) {
                        Map<String, Object> range = new HashMap<>();
                        String dateKey = rangeStartDate.equals(rangeEndDate) ?
                                rangeStartDate : rangeStartDate + " - " + rangeEndDate;
                        range.put("dateRange", dateKey);
                        range.put("totalSalesL", Math.round(currentRangeTotal * 100.0) / 100.0);
                        priceRanges.add(range);
                    }

                    // Start new range
                    rangeStartDate = currentDate;
                    currentRangeTotal = salesL;
                } else {
                    // Continue current range or start first range
                    if (rangeStartDate == null) {
                        rangeStartDate = currentDate;
                        currentRangeTotal = salesL;
                    } else {
                        currentRangeTotal += salesL;
                    }
                }

                rangeEndDate = currentDate;
                lastRate = rate;
            }
        }

        // Finalize last range
        if (rangeStartDate != null) {
            Map<String, Object> range = new HashMap<>();
            String dateKey = rangeStartDate.equals(rangeEndDate) ?
                    rangeStartDate : rangeStartDate + " - " + rangeEndDate;
            range.put("dateRange", dateKey);
            range.put("totalSalesL", Math.round(currentRangeTotal * 100.0) / 100.0);
            priceRanges.add(range);
        }

        // Build result - always use date ranges for consistency
        if (priceRanges.isEmpty()) {
            return Map.of("totalSales", 0.0);
        } else {
            // Always use date range format for consistency
            Map<String, Object> result = new HashMap<>();
            for (Map<String, Object> range : priceRanges) {
                String dateRange = (String) range.get("dateRange");
                Double totalSalesL = (Double) range.get("totalSalesL");
                result.put(dateRange, Map.of("totalSalesL", totalSalesL));
            }
            return result;
        }
    }

    private Map<String, Object> generateRateData(List<DailySales> dailySalesData, List<Supply> supplyData, String product) {
        log.info("=== GENERATING {} RATE DATA ===", product);

        Map<String, Object> rateData = new HashMap<>();

        // Filter data for specific product
        List<DailySales> productSales = dailySalesData.stream()
                .filter(sale -> product.equalsIgnoreCase(sale.getProduct()))
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        List<Supply> productSupply = supplyData.stream()
                .filter(supply -> product.equalsIgnoreCase(supply.getProduct()))
                .sorted(Comparator.comparing(Supply::getDate))
                .collect(Collectors.toList());

        // Last supply rate
        double lastSupplyRate = productSupply.stream()
                .max(Comparator.comparing(Supply::getDate))
                .map(supply -> supply.getRate() != null ? supply.getRate() : 0.0)
                .orElse(0.0);

        // Last sales rate
        double lastSalesRate = productSales.stream()
                .max(Comparator.comparing(DailySales::getDate))
                .map(dailySales -> dailySales.getRate() != null ? dailySales.getRate() : 0.0)
                .orElse(0.0);

        log.info("Supply records for {}: {}", product, productSupply.size());
        log.info("Sales records for {}: {}", product, productSales.size());
        log.info("Last supply rate for {}: {}", product, lastSupplyRate);
        log.info("Last sales rate for {}: {}", product, lastSalesRate);

        // Opening stock (from the first day's opening stock)
        double openingStock = productSales.isEmpty() ? 0.0 :
                (productSales.get(0).getOpenSL() != null ? productSales.get(0).getOpenSL() : 0.0);

        // Total supply between dates
        double totalSupply = productSupply.stream()
                .mapToDouble(supply -> supply.getQty() != null ? supply.getQty() : 0.0)
                .sum();

        // Available stock
        double availableStock = openingStock + totalSupply;

        // Sales cost (sum of all salesL)
        double salesCost = productSales.stream()
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();

        // Closing stock
        double closingStock = availableStock - salesCost;

        // Closing dispensing (closingSL of the last date)
        double closingDispensing = productSales.isEmpty() ? 0.0 :
                productSales.stream()
                        .max(Comparator.comparing(DailySales::getDate))
                        .map(sale -> sale.getClosingSL() != null ? sale.getClosingSL() : 0.0)
                        .orElse(0.0);

        // Underground gains
        double undergroundGains = closingDispensing - closingStock;

        // Pump gains (salesCost * 0.0025)
        double pumpGains = salesCost * 0.0025;

        // Generate synchronized sales unit price with rates instead of totalSalesL
        Object salesUnitPriceWithRates = generateSynchronizedRateData(dailySalesData, product);

        rateData.put("openingStock", Math.round(lastSupplyRate * 100.0) / 100.0);
        rateData.put("salesCost", Math.round(lastSupplyRate * 100.0) / 100.0);
        rateData.put("salesUnitPrice", salesUnitPriceWithRates);
        rateData.put("availableStock", Math.round(availableStock * 100.0) / 100.0);
        rateData.put("closingDispensing", Math.round(lastSalesRate * 100.0) / 100.0);
        rateData.put("closingStock", Math.round(closingStock * 100.0) / 100.0);
        rateData.put("pumpGains", Math.round(pumpGains * 100.0) / 100.0);
        rateData.put("supply", Math.round(totalSupply * 100.0) / 100.0);
        rateData.put("undergroundGains", Math.round(lastSalesRate * 100.0) / 100.0);

        log.info("=== RATE DATA RESULT FOR {} ===", product);
        log.info("rateData[openingStock] = {} (this will be used as lastSupplyRate in value calculations)", rateData.get("openingStock"));
        log.info("rateData[closingDispensing] = {} (this will be used as lastSalesRate in value calculations)", rateData.get("closingDispensing"));
        log.info("Complete rate data: {}", rateData);

        return rateData;
    }

    private Object generateSynchronizedRateData(List<DailySales> allDailySalesData, String targetProduct) {
        log.info("Generating synchronized rate data for product: {}", targetProduct);

        if (allDailySalesData.isEmpty()) {
            return Map.of("totalSales", 0.0);
        }

        // Sort all sales by createdAt for proper chronological order
        List<DailySales> sortedSales = allDailySalesData.stream()
                .sorted(Comparator.comparing(DailySales::getCreatedAt))
                .collect(Collectors.toList());

        // Create synchronized rate change points (same logic as sales unit price)
        List<SynchronizedRateChangePoint> synchronizedPoints = identifySynchronizedRateChangePoints(sortedSales);

        // Calculate rates for the target product within each synchronized period
        Map<String, Object> result = new LinkedHashMap<>();

        for (SynchronizedRateChangePoint point : synchronizedPoints) {
            double rate = getRateForSynchronizedPoint(point.getSales(), targetProduct);
            String columnKey = point.getColumnKey();

            if (rate > 0) {
                result.put(columnKey, Map.of("rate", Math.round(rate * 100.0) / 100.0));
                log.info("Rate for {} in period {}: {}", targetProduct, columnKey, rate);
            }
        }

        return result.isEmpty() ? Map.of("totalSales", 0.0) : result;
    }

    private double getRateForSynchronizedPoint(List<DailySales> periodSales, String targetProduct) {
        // Get the rate for the target product in this period
        // Use the first rate found for the product in this period
        return periodSales.stream()
                .filter(sale -> targetProduct.equalsIgnoreCase(sale.getProduct()))
                .filter(sale -> sale.getRate() != null)
                .mapToDouble(DailySales::getRate)
                .findFirst()
                .orElse(0.0);
    }

    private Map<String, Object> generateValueData(List<DailySales> dailySalesData, String product, Map<String, Object> rateData) {
        log.info("=== GENERATING {} VALUE DATA ===", product);

        Map<String, Object> valueData = new HashMap<>();

        // Filter data for specific product
        List<DailySales> productSales = dailySalesData.stream()
                .filter(sale -> product.equalsIgnoreCase(sale.getProduct()))
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        // Get rates
        double lastSupplyRate = (Double) rateData.get("openingStock");
        double lastSalesRate = (Double) rateData.get("closingDispensing");
        
        log.info("Rate data for {}: lastSupplyRate = {}, lastSalesRate = {}", product, lastSupplyRate, lastSalesRate);
        log.info("Rate data structure: {}", rateData);

        // Opening stock value
        double openingStock = productSales.isEmpty() ? 0.0 :
                (productSales.get(0).getOpenSL() != null ? productSales.get(0).getOpenSL() : 0.0);
        double openingStockValue = openingStock * lastSupplyRate;
        
        log.info("Opening stock calculation: {} * {} = {}", openingStock, lastSupplyRate, openingStockValue);

        // Sales cost value
        double salesCost = productSales.stream()
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();
        double salesCostValue = salesCost * lastSupplyRate;
        
        log.info("Sales cost calculation: {} * {} = {}", salesCost, lastSupplyRate, salesCostValue);

        // Sales unit price value (using synchronized approach with date ranges)
        Object salesUnitPriceValueStructured = generateSynchronizedSalesUnitPriceValue(dailySalesData, product, rateData);

        // Closing dispensing value
        double closingDispensing = productSales.isEmpty() ? 0.0 :
                productSales.stream()
                        .max(Comparator.comparing(DailySales::getCreatedAt))
                        .map(sale -> sale.getClosingSL() != null ? sale.getClosingSL() : 0.0)
                        .orElse(0.0);
        double closingDispensingValue = lastSalesRate * closingDispensing;

        // Underground gains value
        double availableStock = openingStock + productSales.stream()
                .mapToDouble(sale -> sale.getSupply() != null ? sale.getSupply() : 0.0)
                .sum();
        double undergroundGains = closingDispensing - (availableStock - salesCost);
        double undergroundGainsValue = undergroundGains * lastSalesRate;

        valueData.put("openingStock", Math.round(openingStockValue * 100.0) / 100.0);
        valueData.put("supply", 0);
        valueData.put("availableStock", 0);
        valueData.put("salesCost", Math.round(salesCostValue * 100.0) / 100.0);
        valueData.put("salesUnitPrice", salesUnitPriceValueStructured);
        valueData.put("closingStock", 0);
        valueData.put("closingDispensing", Math.round(closingDispensingValue * 100.0) / 100.0);
        valueData.put("undergroundGains", Math.round(undergroundGainsValue * 100.0) / 100.0);
        valueData.put("pumpGains", 0);

        return valueData;
    }

    private double calculateSalesUnitPriceValue(List<DailySales> productSales, double lastSupplyRate) {
        Map<Double, Double> rateGroups = new HashMap<>();

        for (DailySales sale : productSales) {
            Double rate = sale.getRate();
            Double salesL = sale.getSalesL();

            if (rate != null && salesL != null) {
                rateGroups.merge(rate, salesL, Double::sum);
            }
        }

        return rateGroups.entrySet().stream()
                .mapToDouble(entry -> entry.getValue() * lastSupplyRate)
                .sum();
    }

    private Map<String, Object> generateTotalData(Map<String, Object> pmsData, Map<String, Object> agoData, List<DailySales> dailySalesData) {
        log.info("Generating total data");

        Map<String, Object> totalData = new HashMap<>();

        totalData.put("openingStock",
                Math.round((getDoubleValue(pmsData.get("openingStock")) + getDoubleValue(agoData.get("openingStock"))) * 100.0) / 100.0);
        totalData.put("supply", getDoubleValue(pmsData.get("supply")) + getDoubleValue(agoData.get("supply"))); // As specified in requirements
        totalData.put("availableStock",
                Math.round((getDoubleValue(pmsData.get("availableStock")) + getDoubleValue(agoData.get("availableStock"))) * 100.0) / 100.0);
        totalData.put("salesCost",
                Math.round((getDoubleValue(pmsData.get("salesCost")) + getDoubleValue(agoData.get("salesCost"))) * 100.0) / 100.0);

        // Combine sales unit prices using synchronized columns
        Map<String, Object> combinedSalesUnitPrice = combinePmsAndAgoSalesUnitPrices(pmsData, agoData);
        totalData.put("salesUnitPrice", combinedSalesUnitPrice);

        totalData.put("closingStock",
                Math.round((getDoubleValue(pmsData.get("closingStock")) + getDoubleValue(agoData.get("closingStock"))) * 100.0) / 100.0);
        totalData.put("closingDispensing",
                Math.round((getDoubleValue(pmsData.get("closingDispensing")) + getDoubleValue(agoData.get("closingDispensing"))) * 100.0) / 100.0);
        totalData.put("undergroundGains",
                Math.round((getDoubleValue(pmsData.get("undergroundGains")) + getDoubleValue(agoData.get("undergroundGains"))) * 100.0) / 100.0);
        totalData.put("pumpGains",
                Math.round((getDoubleValue(pmsData.get("pumpGains")) + getDoubleValue(agoData.get("pumpGains"))) * 100.0) / 100.0);

        return totalData;
    }

    private Map<String, Object> combinePmsAndAgoSalesUnitPrices(Map<String, Object> pmsData, Map<String, Object> agoData) {
        log.info("Combining PMS and AGO sales unit prices for total");

        Map<String, Object> combinedSalesUnitPrice = new HashMap<>();
        Map<String, Double> dateRangeTotals = new LinkedHashMap<>();

        // Get PMS sales unit price data
        Object pmsUnitPrice = pmsData.get("salesUnitPrice");
        if (pmsUnitPrice instanceof Map) {
            Map<String, Object> pmsMap = (Map<String, Object>) pmsUnitPrice;
            for (Map.Entry<String, Object> entry : pmsMap.entrySet()) {
                String dateRange = entry.getKey();
                if (!"totalSales".equals(dateRange) && entry.getValue() instanceof Map) {
                    Map<String, Object> rangeData = (Map<String, Object>) entry.getValue();
                    double totalSalesL = getDoubleValue(rangeData.get("totalSalesL"));
                    dateRangeTotals.put(dateRange, totalSalesL);
                    log.info("Added PMS data for range {}: {}", dateRange, totalSalesL);
                }
            }
        }

        // Get AGO sales unit price data and add to matching date ranges
        Object agoUnitPrice = agoData.get("salesUnitPrice");
        if (agoUnitPrice instanceof Map) {
            Map<String, Object> agoMap = (Map<String, Object>) agoUnitPrice;
            for (Map.Entry<String, Object> entry : agoMap.entrySet()) {
                String dateRange = entry.getKey();
                if (!"totalSales".equals(dateRange) && entry.getValue() instanceof Map) {
                    Map<String, Object> rangeData = (Map<String, Object>) entry.getValue();
                    double totalSalesL = getDoubleValue(rangeData.get("totalSalesL"));
                    // Sum with existing PMS data or create new entry
                    dateRangeTotals.merge(dateRange, totalSalesL, Double::sum);
                    log.info("Added AGO data for range {}: {} (total now: {})",
                            dateRange, totalSalesL, dateRangeTotals.get(dateRange));
                }
            }
        }

        log.info("Combined date range totals: {}", dateRangeTotals);

        // Build the final result structure using actual date ranges as keys
        if (dateRangeTotals.isEmpty()) {
            // Check if both PMS and AGO have totalSales instead of date ranges
            double pmsTotalSales = 0.0;
            double agoTotalSales = 0.0;

            if (pmsUnitPrice instanceof Map) {
                Map<String, Object> pmsMap = (Map<String, Object>) pmsUnitPrice;
                pmsTotalSales = getDoubleValue(pmsMap.get("totalSales"));
            }

            if (agoUnitPrice instanceof Map) {
                Map<String, Object> agoMap = (Map<String, Object>) agoUnitPrice;
                agoTotalSales = getDoubleValue(agoMap.get("totalSales"));
            }

            double totalCombined = pmsTotalSales + agoTotalSales;
            combinedSalesUnitPrice.put("totalSales", Math.round(totalCombined * 100.0) / 100.0);
            log.info("No date ranges found, using totalSales: PMS={}, AGO={}, Combined={}",
                    pmsTotalSales, agoTotalSales, totalCombined);
        } else if (dateRangeTotals.size() == 1) {
            // Single date range, return as totalSales
            double totalSales = dateRangeTotals.values().iterator().next();
            combinedSalesUnitPrice.put("totalSales", Math.round(totalSales * 100.0) / 100.0);
            log.info("Single date range, totalSales: {}", totalSales);
        } else {
            // Multiple date ranges, use date ranges as keys
            for (Map.Entry<String, Double> entry : dateRangeTotals.entrySet()) {
                String dateRangeKey = entry.getKey();
                double total = entry.getValue();
                combinedSalesUnitPrice.put(dateRangeKey, Map.of("totalSalesL", Math.round(total * 100.0) / 100.0));
                log.info("Added total for date range {}: {}", dateRangeKey, total);
            }
        }

        log.info("Final combined sales unit price: {}", combinedSalesUnitPrice);
        return combinedSalesUnitPrice;
    }

    private Map<String, Object> generateTotalValuesData(Map<String, Object> pmsValueData, Map<String, Object> agoValueData) {
        log.info("Generating total values data");

        Map<String, Object> totalValuesData = new HashMap<>();

        totalValuesData.put("openingStock",
                Math.round((getDoubleValue(pmsValueData.get("openingStock")) + getDoubleValue(agoValueData.get("openingStock"))) * 100.0) / 100.0);
        totalValuesData.put("supply", 0); // As specified in requirements
        totalValuesData.put("availableStock",
                Math.round((getDoubleValue(pmsValueData.get("availableStock")) + getDoubleValue(agoValueData.get("availableStock"))) * 100.0) / 100.0);
        totalValuesData.put("salesCost",
                Math.round((getDoubleValue(pmsValueData.get("salesCost")) + getDoubleValue(agoValueData.get("salesCost"))) * 100.0) / 100.0);

        // Combine sales unit prices by summing values for matching date ranges
        Map<String, Object> combinedSalesUnitPrice = combinePmsAndAgoValueSalesUnitPrices(pmsValueData, agoValueData);
        totalValuesData.put("salesUnitPrice", combinedSalesUnitPrice);

        totalValuesData.put("closingStock",
                Math.round((getDoubleValue(pmsValueData.get("closingStock")) + getDoubleValue(agoValueData.get("closingStock"))) * 100.0) / 100.0);
        totalValuesData.put("closingDispensing",
                Math.round((getDoubleValue(pmsValueData.get("closingDispensing")) + getDoubleValue(agoValueData.get("closingDispensing"))) * 100.0) / 100.0);
        totalValuesData.put("undergroundGains",
                Math.round((getDoubleValue(pmsValueData.get("undergroundGains")) + getDoubleValue(agoValueData.get("undergroundGains"))) * 100.0) / 100.0);
        totalValuesData.put("pumpGains",
                Math.round((getDoubleValue(pmsValueData.get("pumpGains")) + getDoubleValue(agoValueData.get("pumpGains"))) * 100.0) / 100.0);

        return totalValuesData;
    }

    private Map<String, Object> combinePmsAndAgoValueSalesUnitPrices(Map<String, Object> pmsValueData, Map<String, Object> agoValueData) {
        log.info("=== COMBINING PMS AND AGO VALUE SALES UNIT PRICES ===");

        Map<String, Object> combinedSalesUnitPrice = new HashMap<>();
        Map<String, Double> dateRangeValueTotals = new LinkedHashMap<>();

        // Get PMS value sales unit price data
        Object pmsUnitPrice = pmsValueData.get("salesUnitPrice");
        if (pmsUnitPrice instanceof Map) {
            Map<String, Object> pmsMap = (Map<String, Object>) pmsUnitPrice;
            for (Map.Entry<String, Object> entry : pmsMap.entrySet()) {
                String dateRange = entry.getKey();
                if (!"totalSales".equals(dateRange) && entry.getValue() instanceof Map) {
                    Map<String, Object> rangeData = (Map<String, Object>) entry.getValue();
                    double value = getDoubleValue(rangeData.get("value"));
                    dateRangeValueTotals.put(dateRange, value);
                    log.info("Added PMS value for range {}: {}", dateRange, value);
                }
            }
        }

        // Get AGO value sales unit price data and add to matching date ranges
        Object agoUnitPrice = agoValueData.get("salesUnitPrice");
        if (agoUnitPrice instanceof Map) {
            Map<String, Object> agoMap = (Map<String, Object>) agoUnitPrice;
            for (Map.Entry<String, Object> entry : agoMap.entrySet()) {
                String dateRange = entry.getKey();
                if (!"totalSales".equals(dateRange) && entry.getValue() instanceof Map) {
                    Map<String, Object> rangeData = (Map<String, Object>) entry.getValue();
                    double value = getDoubleValue(rangeData.get("value"));
                    // Sum with existing PMS data or create new entry
                    dateRangeValueTotals.merge(dateRange, value, Double::sum);
                    log.info("Added AGO value for range {}: {} (total now: {})",
                            dateRange, value, dateRangeValueTotals.get(dateRange));
                }
            }
        }

        log.info("Combined date range value totals: {}", dateRangeValueTotals);

        // Build the final result structure using actual date ranges as keys
        if (dateRangeValueTotals.isEmpty()) {
            // Check if both PMS and AGO have totalSales instead of date ranges
            double pmsTotalValue = 0.0;
            double agoTotalValue = 0.0;

            if (pmsUnitPrice instanceof Map) {
                Map<String, Object> pmsMap = (Map<String, Object>) pmsUnitPrice;
                pmsTotalValue = getDoubleValue(pmsMap.get("totalSales"));
            }

            if (agoUnitPrice instanceof Map) {
                Map<String, Object> agoMap = (Map<String, Object>) agoUnitPrice;
                agoTotalValue = getDoubleValue(agoMap.get("totalSales"));
            }

            double totalCombined = pmsTotalValue + agoTotalValue;
            combinedSalesUnitPrice.put("totalSales", Math.round(totalCombined * 100.0) / 100.0);
            log.info("No date ranges found, using totalSales: PMS={}, AGO={}, Combined={}",
                    pmsTotalValue, agoTotalValue, totalCombined);
        } else {
            // Use date ranges as keys with summed values
            for (Map.Entry<String, Double> entry : dateRangeValueTotals.entrySet()) {
                String dateRangeKey = entry.getKey();
                double total = entry.getValue();
                combinedSalesUnitPrice.put(dateRangeKey, Map.of("value", Math.round(total * 100.0) / 100.0));
                log.info("Added total value for date range {}: {}", dateRangeKey, Math.round(total * 100.0) / 100.0);
            }
        }

        log.info("=== FINAL COMBINED VALUE SALES UNIT PRICE ===");
        log.info("Result structure: {}", combinedSalesUnitPrice);
        return combinedSalesUnitPrice;
    }

    /**
     * Safely converts a Number object to Double, handling both Integer and Double types
     */
    private double getDoubleValue(Object value) {
        if (value == null) {
            return 0.0;
        }
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return 0.0;
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

    private Object generateSynchronizedSalesUnitPrice(List<DailySales> allDailySalesData, String targetProduct) {
        log.info("Generating synchronized sales unit price for product: {}", targetProduct);

        if (allDailySalesData.isEmpty()) {
            return Map.of("totalSales", 0.0);
        }

        // Sort all sales by createdAt for proper chronological order
        List<DailySales> sortedSales = allDailySalesData.stream()
                .sorted(Comparator.comparing(DailySales::getCreatedAt))
                .collect(Collectors.toList());

        log.info("=== DEBUGGING SYNCHRONIZED SALES UNIT PRICE FOR {} ===", targetProduct);
        for (DailySales sale : sortedSales) {
            log.info("Sale: Date={}, Product={}, Rate={}, SalesL={}, CreatedAt={}",
                    sale.getDate(), sale.getProduct(), sale.getRate(), sale.getSalesL(), sale.getCreatedAt());
        }

        // Create exactly 6 synchronized rate change points
        List<SynchronizedRateChangePoint> synchronizedPoints = identifySynchronizedRateChangePoints(sortedSales);

        log.info("Identified {} synchronized rate change points", synchronizedPoints.size());

        // Calculate totals for the target product within each synchronized period
        Map<String, Object> result = new LinkedHashMap<>();

        for (int i = 0; i < synchronizedPoints.size(); i++) {
            SynchronizedRateChangePoint point = synchronizedPoints.get(i);
            double totalSalesL = calculateSalesLForSynchronizedPoint(point.getSales(), targetProduct);
            String columnKey = point.getColumnKey();

            result.put(columnKey, Map.of("totalSalesL", Math.round(totalSalesL * 100.0) / 100.0));
            log.info("Column {} for {}: totalSalesL = {} (date range: {})",
                    columnKey, targetProduct, totalSalesL, point.getDateRange());
        }

        return result.isEmpty() ? Map.of("totalSales", 0.0) : result;
    }

    private List<SynchronizedRateChangePoint> identifySynchronizedRateChangePoints(List<DailySales> sortedSales) {
        List<SynchronizedRateChangePoint> synchronizedPoints = new ArrayList<>();

        if (sortedSales.isEmpty()) {
            return synchronizedPoints;
        }

        log.info("=== IDENTIFYING SYNCHRONIZED RATE CHANGE POINTS (CHRONOLOGICAL BY CREATED_AT) ===");

        // Track current rates for each product and current period
        Map<String, Double> currentRates = new HashMap<>();
        String rangeStartDate = null;
        String rangeEndDate = null;
        List<DailySales> currentPeriodSales = new ArrayList<>();

        // Process sales chronologically by createdAt (already sorted)
        for (int i = 0; i < sortedSales.size(); i++) {
            DailySales sale = sortedSales.get(i);
            String product = sale.getProduct();
            Double rate = sale.getRate();
            String currentDate = sale.getDate();

            if (rate == null) {
                continue;
            }

            // Check if this sale represents a rate change
            Double previousRate = currentRates.get(product);
            boolean rateChanged = previousRate != null && !rate.equals(previousRate);

            if (rateChanged) {
                log.info("Rate change detected for {} on {} at {}: {} -> {}",
                        product, currentDate, sale.getCreatedAt(), previousRate, rate);

                // Only finalize if we have accumulated sales (not just starting a new period)
                if (!currentPeriodSales.isEmpty() && rangeStartDate != null) {
                    // Check if the previous period has more than one entry or covers more than one date
                    boolean shouldFinalize = currentPeriodSales.size() > 1 ||
                            !rangeStartDate.equals(rangeEndDate);

                    if (shouldFinalize) {
                        log.info("Finalizing period before rate change: {} to {} ({} sales)",
                                rangeStartDate, rangeEndDate, currentPeriodSales.size());
                        finalizeSynchronizedPeriod(synchronizedPoints, rangeStartDate, rangeEndDate, currentPeriodSales);

                        // Start new period
                        currentPeriodSales = new ArrayList<>();
                        rangeStartDate = currentDate;
                        rangeEndDate = currentDate;
                    } else {
                        // If it's just one sale, continue accumulating
                        log.info("Continuing to accumulate sales for period starting at {}", rangeStartDate);
                        rangeEndDate = currentDate;
                    }
                } else {
                    // First sale or empty period, just start tracking
                    rangeStartDate = currentDate;
                    rangeEndDate = currentDate;
                }
            } else {
                // Initialize range start if not set
                if (rangeStartDate == null) {
                    rangeStartDate = currentDate;
                }

                // Update end date
                rangeEndDate = currentDate;
            }

            // Add this sale to current period
            currentPeriodSales.add(sale);

            // Update current rate for this product
            currentRates.put(product, rate);
        }

        // Finalize the last period
        if (!currentPeriodSales.isEmpty()) {
            log.info("Finalizing last period: {} to {}", rangeStartDate, rangeEndDate);
            finalizeSynchronizedPeriod(synchronizedPoints, rangeStartDate, rangeEndDate, currentPeriodSales);
        }

        log.info("Generated {} synchronized points", synchronizedPoints.size());
        for (int i = 0; i < synchronizedPoints.size(); i++) {
            SynchronizedRateChangePoint point = synchronizedPoints.get(i);
            log.info("Point {}: {} ({} sales records)",
                    i + 1, point.getDateRange(), point.getSales().size());

            // Log sales details for each period
            Map<String, Double> periodSalesByProduct = point.getSales().stream()
                    .collect(Collectors.groupingBy(DailySales::getProduct,
                            Collectors.summingDouble(s -> s.getSalesL() != null ? s.getSalesL() : 0.0)));
            log.info("  Sales by product: {}", periodSalesByProduct);
        }

        return synchronizedPoints;
    }

    private void finalizeSynchronizedPeriod(List<SynchronizedRateChangePoint> synchronizedPoints,
                                            String rangeStartDate, String rangeEndDate, List<DailySales> periodSales) {
        if (periodSales.isEmpty()) return;

        String dateRange = rangeStartDate.equals(rangeEndDate) ? rangeStartDate : rangeStartDate + " - " + rangeEndDate;
        String columnKey = dateRange;

        SynchronizedRateChangePoint point = new SynchronizedRateChangePoint(
                columnKey, dateRange, new ArrayList<>(periodSales));
        synchronizedPoints.add(point);

        log.info("Finalized synchronized period: {} with {} sales", dateRange, periodSales.size());

        // Log detailed breakdown for debugging
        Map<String, List<DailySales>> salesByProduct = periodSales.stream()
                .collect(Collectors.groupingBy(DailySales::getProduct));

        for (Map.Entry<String, List<DailySales>> entry : salesByProduct.entrySet()) {
            String product = entry.getKey();
            List<DailySales> productSales = entry.getValue();
            double totalSalesL = productSales.stream()
                    .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                    .sum();
            log.info("  {} total salesL in period {}: {}", product, dateRange, totalSalesL);
        }
    }

    private double calculateSalesLForSynchronizedPoint(List<DailySales> periodSales, String targetProduct) {
        return periodSales.stream()
                .filter(sale -> targetProduct.equalsIgnoreCase(sale.getProduct()))
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();
    }

    private Object generateSynchronizedSalesUnitPriceValue(List<DailySales> allDailySalesData, String targetProduct, Map<String, Object> rateData) {
        log.info("=== GENERATING SYNCHRONIZED SALES UNIT PRICE VALUE FOR {} ===", targetProduct);
        log.info("Calculation: respective pms_rate salesUnitPrice * pms salesUnitPrice");

        if (allDailySalesData.isEmpty()) {
            log.info("No daily sales data available, returning default");
            return Map.of("totalSales", 0.0);
        }

        // Get the regular sales unit price structure for this product
        Object regularSalesUnitPriceObj = generateSynchronizedSalesUnitPrice(allDailySalesData, targetProduct);
        
        log.info("Regular sales unit price structure for {}: {}", targetProduct, regularSalesUnitPriceObj);

        // Get the rate structure from the rate data
        Object rateUnitPriceStructureObj = rateData.get("salesUnitPrice");
        
        log.info("Rate unit price structure for {}: {}", targetProduct, rateUnitPriceStructureObj);

        Map<String, Object> result = new LinkedHashMap<>();

        // Iterate through each date range and calculate value = rate * salesL
        if (regularSalesUnitPriceObj instanceof Map && rateUnitPriceStructureObj instanceof Map) {
            Map<String, Object> regularSalesUnitPrice = (Map<String, Object>) regularSalesUnitPriceObj;
            Map<String, Object> rateUnitPriceStructure = (Map<String, Object>) rateUnitPriceStructureObj;
            
            for (Map.Entry<String, Object> entry : regularSalesUnitPrice.entrySet()) {
                String dateRange = entry.getKey();
                
                if (!"totalSales".equals(dateRange) && entry.getValue() instanceof Map) {
                    Map<String, Object> salesData = (Map<String, Object>) entry.getValue();
                    double totalSalesL = getDoubleValue(salesData.get("totalSalesL"));
                    
                    // Get corresponding rate from rate structure
                    double rate = 0.0;
                    if (rateUnitPriceStructure.containsKey(dateRange)) {
                        Object rateDataForRange = rateUnitPriceStructure.get(dateRange);
                        if (rateDataForRange instanceof Map) {
                            Map<String, Object> rateMap = (Map<String, Object>) rateDataForRange;
                            rate = getDoubleValue(rateMap.get("rate"));
                        }
                    }
                    
                    double value = rate * totalSalesL;
                    
                    log.info("=== CALCULATION FOR PERIOD {} ===", dateRange);
                    log.info("Rate from rate data: {}", rate);
                    log.info("TotalSalesL from regular data: {}", totalSalesL);
                    log.info("Value calculation: {} * {} = {}", rate, totalSalesL, value);

                    if (value > 0) {
                        double roundedValue = Math.round(value * 100.0) / 100.0;
                        result.put(dateRange, Map.of("value", roundedValue));
                        log.info("Added to result: {} -> {}", dateRange, roundedValue);
                    } else {
                        log.info("Skipping period {} as value is 0 (rate={}, salesL={})", dateRange, rate, totalSalesL);
                    }
                }
            }
        } else {
            log.warn("Invalid data structures - regularSalesUnitPrice: {}, rateUnitPriceStructure: {}", 
                    regularSalesUnitPriceObj.getClass().getSimpleName(), 
                    rateUnitPriceStructureObj != null ? rateUnitPriceStructureObj.getClass().getSimpleName() : "null");
        }

        log.info("=== FINAL RESULT FOR {} VALUE CALCULATION ===", targetProduct);
        log.info("Result structure: {}", result);

        return result.isEmpty() ? Map.of("totalSales", 0.0) : result;
    }

    private List<SynchronizedRateChangePoint> ensureExactly6Columns(List<SynchronizedRateChangePoint> points) {
        if (points.size() == 6) {
            return points;
        }

        if (points.size() < 6) {
            // If we have fewer than 6, we need to split some periods
            // For now, return as is - this should be handled by better logic above
            log.warn("Only {} synchronized points found, expected 6", points.size());
            return points;
        } else {
            // If we have more than 6, consolidate the smaller ones
            log.info("Consolidating {} points down to 6", points.size());

            // Sort by total sales volume to keep the largest periods
            points.sort((a, b) -> Double.compare(
                    b.getSales().stream().mapToDouble(s -> s.getSalesL() != null ? s.getSalesL() : 0.0).sum(),
                    a.getSales().stream().mapToDouble(s -> s.getSalesL() != null ? s.getSalesL() : 0.0).sum()
            ));

            // Keep the 6 largest periods
            List<SynchronizedRateChangePoint> consolidated = new ArrayList<>(points.subList(0, 6));

            // Re-sort by date range for proper ordering
            consolidated.sort(Comparator.comparing(SynchronizedRateChangePoint::getDateRange));

            return consolidated;
        }
    }

    // Inner class to represent synchronized rate change points
    private static class SynchronizedRateChangePoint {
        private final String columnKey;
        private final String dateRange;
        private final List<DailySales> sales;

        public SynchronizedRateChangePoint(String columnKey, String dateRange, List<DailySales> sales) {
            this.columnKey = columnKey;
            this.dateRange = dateRange;
            this.sales = sales;
        }

        public String getColumnKey() {
            return columnKey;
        }

        public String getDateRange() {
            return dateRange;
        }

        public List<DailySales> getSales() {
            return sales;
        }
    }

    private Map<String, Object> generateProductDataWithSynchronization(List<DailySales> dailySalesData, List<Supply> supplyData, String product) {
        log.info("Generating {} product data with synchronization", product);

        Map<String, Object> productData = new HashMap<>();

        // Filter data for specific product
        List<DailySales> productSales = dailySalesData.stream()
                .filter(sale -> product.equalsIgnoreCase(sale.getProduct()))
                .sorted(Comparator.comparing(DailySales::getDate))
                .collect(Collectors.toList());

        List<Supply> productSupply = supplyData.stream()
                .filter(supply -> product.equalsIgnoreCase(supply.getProduct()))
                .sorted(Comparator.comparing(Supply::getDate))
                .collect(Collectors.toList());

        // Opening stock (from the first day's opening stock)
        double openingStock = productSales.isEmpty() ? 0.0 :
                (productSales.get(0).getOpenSL() != null ? productSales.get(0).getOpenSL() : 0.0);

        // Total supply between dates
        double totalSupply = productSupply.stream()
                .mapToDouble(supply -> supply.getQty() != null ? supply.getQty() : 0.0)
                .sum();

        // Available stock
        double availableStock = openingStock + totalSupply;

        // Sales cost (sum of all salesL)
        double salesCost = productSales.stream()
                .mapToDouble(sale -> sale.getSalesL() != null ? sale.getSalesL() : 0.0)
                .sum();

        // Sales unit price (using synchronized approach)
        Object salesUnitPrice = generateSynchronizedSalesUnitPrice(dailySalesData, product);

        // Closing stock
        double closingStock = availableStock - salesCost;

        // Closing dispensing (closingSL of the last date)
        double closingDispensing = productSales.isEmpty() ? 0.0 :
                productSales.stream()
                        .max(Comparator.comparing(DailySales::getCreatedAt))
                        .map(sale -> sale.getClosingSL() != null ? sale.getClosingSL() : 0.0)
                        .orElse(0.0);

        // Underground gains
        double undergroundGains = closingDispensing - closingStock;

        // Pump gains (salesCost * 0.0025)
        double pumpGains = salesCost * 0.0025;

        productData.put("openingStock", Math.round(openingStock * 100.0) / 100.0);
        productData.put("supply", Math.round(totalSupply * 100.0) / 100.0);
        productData.put("availableStock", Math.round(availableStock * 100.0) / 100.0);
        productData.put("salesCost", Math.round(salesCost * 100.0) / 100.0);
        productData.put("salesUnitPrice", salesUnitPrice);
        productData.put("closingStock", Math.round(closingStock * 100.0) / 100.0);
        productData.put("closingDispensing", Math.round(closingDispensing * 100.0) / 100.0);
        productData.put("undergroundGains", Math.round(undergroundGains * 100.0) / 100.0);
        productData.put("pumpGains", Math.round(pumpGains * 100.0) / 100.0);

        log.info("{} data generated - Opening: {}, Supply: {}, Sales: {}",
                product, openingStock, totalSupply, salesCost);

        return productData;
    }

    private Map<String, Object> generateTotalDataWithSynchronizedColumns(Map<String, Object> pmsData, Map<String, Object> agoData, List<DailySales> dailySalesData) {
        log.info("Generating total data with synchronized columns");

        Map<String, Object> totalData = new HashMap<>();

        totalData.put("openingStock",
                Math.round((getDoubleValue(pmsData.get("openingStock")) + getDoubleValue(agoData.get("openingStock"))) * 100.0) / 100.0);
        totalData.put("supply", getDoubleValue(pmsData.get("supply")) + getDoubleValue(agoData.get("supply"))); // As specified in requirements
        totalData.put("availableStock",
                Math.round((getDoubleValue(pmsData.get("availableStock")) + getDoubleValue(agoData.get("availableStock"))) * 100.0) / 100.0);
        totalData.put("salesCost",
                Math.round((getDoubleValue(pmsData.get("salesCost")) + getDoubleValue(agoData.get("salesCost"))) * 100.0) / 100.0);

        // Combine sales unit prices using synchronized columns
        Map<String, Object> combinedSalesUnitPrice = combinePmsAndAgoSalesUnitPrices(pmsData, agoData);
        totalData.put("salesUnitPrice", combinedSalesUnitPrice);

        totalData.put("closingStock",
                Math.round((getDoubleValue(pmsData.get("closingStock")) + getDoubleValue(agoData.get("closingStock"))) * 100.0) / 100.0);
        totalData.put("closingDispensing",
                Math.round((getDoubleValue(pmsData.get("closingDispensing")) + getDoubleValue(agoData.get("closingDispensing"))) * 100.0) / 100.0);
        totalData.put("undergroundGains",
                Math.round((getDoubleValue(pmsData.get("undergroundGains")) + getDoubleValue(agoData.get("undergroundGains"))) * 100.0) / 100.0);
        totalData.put("pumpGains",
                Math.round((getDoubleValue(pmsData.get("pumpGains")) + getDoubleValue(agoData.get("pumpGains"))) * 100.0) / 100.0);

        return totalData;
    }

    private Map<String, Object> generateSummary(List<DailySales> dailySalesData, List<Supply> supplyData, Map<String, Object> totalValuesData) {
        log.info("=== GENERATING SUMMARY ===");
        
        Map<String, Object> summary = new HashMap<>();

        // 1. openingStockValue: totalValues openStock data
        double openingStockValue = getDoubleValue(totalValuesData.get("openingStock"));
        summary.put("openingStockValue", Math.round(openingStockValue * 100.0) / 100.0);
        log.info("Opening Stock Value: {}", openingStockValue);

        // 2. totalSupplyValue: sum supply data amountCost
        double totalSupplyValue = supplyData.stream()
                .mapToDouble(supply -> supply.getAmountCost() != null ? supply.getAmountCost() : 0.0)
                .sum();
        summary.put("totalSupplyValue", Math.round(totalSupplyValue * 100.0) / 100.0);
        log.info("Total Supply Value: {}", totalSupplyValue);

        // 3. availableStockValue: openingStockValue + totalSupplyValue
        double availableStockValue = openingStockValue + totalSupplyValue;
        summary.put("availableStockValue", Math.round(availableStockValue * 100.0) / 100.0);
        log.info("Available Stock Value: {}", availableStockValue);

        // 4. salesValue: sum daily sales value for the dates
        double salesValue = dailySalesData.stream()
                .mapToDouble(sale -> sale.getValue() != null ? sale.getValue() : 0.0)
                .sum();
        summary.put("salesValue", Math.round(salesValue * 100.0) / 100.0);
        log.info("Sales Value: {}", salesValue);

        // 5. closingStockValue: total value closingDispensing
        double closingStockValue = getDoubleValue(totalValuesData.get("closingDispensing"));
        summary.put("closingStockValue", Math.round(closingStockValue * 100.0) / 100.0);
        log.info("Closing Stock Value: {}", closingStockValue);

        // 6. saleClosingStock: salesValue + closingStockValue
        double saleClosingStock = salesValue + closingStockValue;
        summary.put("saleClosingStock", Math.round(saleClosingStock * 100.0) / 100.0);
        log.info("Sale Closing Stock: {}", saleClosingStock);

        // 7. expectedProfit: availableStockValue - saleClosingStock
        double expectedProfit = saleClosingStock - availableStockValue;
        summary.put("expectedProfit", Math.round(expectedProfit * 100.0) / 100.0);
        log.info("Expected Profit: {}", expectedProfit);

        // 8. salesProfit: salesValue - totalValue salesCost
        double totalValuesSalesCost = getDoubleValue(totalValuesData.get("salesCost"));
        double salesProfit = salesValue - totalValuesSalesCost;
        summary.put("salesProfit", Math.round(salesProfit * 100.0) / 100.0);
        log.info("Sales Profit: {} (salesValue: {} - salesCost: {})", salesProfit, salesValue, totalValuesSalesCost);

        // 9. undergroundGains/Loss: totalValue undergroundGains
        double undergroundGainsLoss = getDoubleValue(totalValuesData.get("undergroundGains"));
        summary.put("undergroundGainsLoss", Math.round(undergroundGainsLoss * 100.0) / 100.0);
        log.info("Underground Gains/Loss: {}", undergroundGainsLoss);

        // 10. winfall: to be done later
        summary.put("winfall", "To be implemented later");

        // 11. shortfall: to be done later
        summary.put("shortfall", "To be implemented later");

        // 12. advances: advances from dailysales data
        double advances = dailySalesData.stream()
                .mapToDouble(sale -> sale.getAdvances() != null ? sale.getAdvances() : 0.0)
                .sum();
        summary.put("advances", Math.round(advances * 100.0) / 100.0);
        log.info("Advances: {}", advances);

        // 13. credit: to be done later
        summary.put("credit", "To be implemented later");

        // 14. ecash: to be done later
        summary.put("ecash", "To be implemented later");

        // 15. momo shortage refund: to be done later
        summary.put("momoShortageRefund", "To be implemented later");

        // 16. advanceRefund: repaymentAdvances from dailysales data
        double advanceRefund = dailySalesData.stream()
                .mapToDouble(sale -> sale.getRepaymentAdvances() != null ? sale.getRepaymentAdvances() : 0.0)
                .sum();
        summary.put("advanceRefund", Math.round(advanceRefund * 100.0) / 100.0);
        log.info("Advance Refund: {}", advanceRefund);

        // 17. creditRefund: to be done later
        summary.put("creditRefund", "To be implemented later");

        // 18. expectedLodgement: salesValue - advances - credit - ecash + momoShortageRefund + advanceRefund + creditRefund
        // For now, using 0 for credit, ecash, momoShortageRefund, creditRefund as they are not implemented yet
        double credit = 0.0; // To be implemented later
        double ecash = 0.0; // To be implemented later
        double momoShortageRefund = 0.0; // To be implemented later
        double creditRefund = 0.0; // To be implemented later
        
        double expectedLodgement = salesValue - advances - credit - ecash + momoShortageRefund + advanceRefund + creditRefund;
        summary.put("expectedLodgement", Math.round(expectedLodgement * 100.0) / 100.0);
        log.info("Expected Lodgement: {} (salesValue: {} - advances: {} - credit: {} - ecash: {} + momoShortageRefund: {} + advanceRefund: {} + creditRefund: {})", 
                expectedLodgement, salesValue, advances, credit, ecash, momoShortageRefund, advanceRefund, creditRefund);

        // 19. actualLodgement: sum bankLodgement from dailySales
        double actualLodgement = dailySalesData.stream()
                .mapToDouble(sale -> sale.getBankLodgement() != null ? sale.getBankLodgement() : 0.0)
                .sum();
        summary.put("actualLodgement", Math.round(actualLodgement * 100.0) / 100.0);
        log.info("Actual Lodgement: {}", actualLodgement);

        // 20. difference: actualLodgement - expectedLodgement
        double difference = actualLodgement - expectedLodgement;
        summary.put("difference", Math.round(difference * 100.0) / 100.0);
        log.info("Difference: {}", difference);

        log.info("=== SUMMARY GENERATION COMPLETE ===");
        log.info("Summary structure: {}", summary);

        return summary;
    }
}
