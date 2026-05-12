package com.kodsonApp.DTO;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesWithCashToBankResponse {
    private String id;
    private String station;
    private String date;
    private String product;
    
    // Sales volume and inventory data
    private Double openSL;
    private Double supply;
    private Double overageShortageL;
    private Double availableL;
    private Double closingSL;
    private Double differenceL;
    private Double checkL;
    private Double openSR;
    private Double closingSR;
    private Double returnTT;
    private Double salesL;
    
    // Financial data
    private Double rate;
    private Double value;
    private Double cashSales;
    private Double creditSales;
    private Double advances;
    private Double shortageMomo;
    private Double cashAvailable;
    private Double repaymentShortageMomo;
    private Double actualCash;
    private Double bankLodgement;
    private Double cashToBank;
    private Double receivedFromDebtors;
    private Double repaymentAdvances;
    
    // Metadata
    private String enteredBy;
    private LocalDateTime createdAt;
    private LocalDateTime targetDateTime;
    
    // Additional product data for PMS and AGO if available
    private Map<String, ProductCashToBankData> productData;
    
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductCashToBankData {
        private String productName;
        private Double cashToBank;
        private Double salesL;
        private Double value;
        private Double openSL;
        private Double closingSL;
        private Double actualCash;
        private LocalDateTime recordDate;
        private String recordId;
    }
}