# Summary Calculations Documentation

## Overview
The ReportingService now includes comprehensive summary calculations that provide key financial insights for station operations.

## Summary Fields

### Stock and Supply Calculations
1. **openingStockValue**: Value from totalValues.openingStock - represents the monetary value of stock at the beginning of the period
2. **totalSupplyValue**: Sum of all supply.amountCost for the period - total cost of fuel supplied
3. **availableStockValue**: openingStockValue + totalSupplyValue - total value available for sale

### Sales and Revenue Calculations
4. **salesValue**: Sum of all dailySales.value for the period - total sales revenue
5. **closingStockValue**: Value from totalValues.closingDispensing - remaining stock value at period end
6. **saleClosingStock**: salesValue + closingStockValue - combined sales and remaining stock value

### Profit and Loss Analysis
7. **expectedProfit**: availableStockValue - saleClosingStock - theoretical profit calculation
8. **salesProfit**: salesValue - totalValues.salesCost - actual sales profit margin
9. **undergroundGainsLoss**: Value from totalValues.undergroundGains - gains/losses from underground storage

### Financial Transactions
10. **advances**: Sum of dailySales.advances - total advances given
11. **advanceRefund**: Sum of dailySales.repaymentAdvances - advances repaid
12. **expectedLodgement**: Sum of dailySales.value - expected bank deposits
13. **actualLodgement**: Sum of dailySales.cashToBank - actual bank deposits
14. **difference**: actualLodgement - expectedLodgement - variance in deposits

### Future Implementation Fields
- **winfall**: To be implemented later
- **shortfall**: To be implemented later  
- **credit**: To be implemented later
- **ecash**: To be implemented later
- **momoShortageRefund**: To be implemented later
- **creditRefund**: To be implemented later

## Data Sources
- **DailySales**: Primary source for sales transactions, advances, and banking data
- **Supply**: Source for supply costs and quantities
- **TotalValues**: Aggregated calculations from PMS and AGO value data

## Usage
The summary is automatically generated with each station report and provides a comprehensive financial overview for the specified date range.

## Example Structure
```json
{
  "totals": {
    // ... existing totals data
  },
  "summary": {
    "openingStockValue": 167060.0,
    "totalSupplyValue": 45000.0,
    "availableStockValue": 212060.0,
    "salesValue": 172742.14,
    "closingStockValue": 197638.0,
    "saleClosingStock": 370380.14,
    "expectedProfit": -158320.14,
    "salesProfit": 19474.61,
    "undergroundGainsLoss": 2769.31,
    "advances": 5000.0,
    "advanceRefund": 2000.0,
    "expectedLodgement": 172742.14,
    "actualLodgement": 165000.0,
    "difference": -7742.14
  }
}
```