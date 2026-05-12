# Example Summary Response

Based on the data structure you provided, here's how the API response will now look with the added summary section:

```json
{
  "totals": {
    "pms_rate": {
      "salesUnitPrice": {
        "2025-10-12": { "rate": 11.75 },
        "2025-10-13 - 2025-10-18": { "rate": 11.65 },
        "2025-10-19 - 2025-10-23": { "rate": 11.9 },
        "2025-10-23 - 2025-10-24": { "rate": 11.44 },
        "2025-10-25 - 2025-10-26": { "rate": 11.97 },
        "2025-10-26": { "rate": 12.55 }
      },
      // ... other pms_rate fields
    },
    // ... other totals sections (pms, ago, ago_value, ago_rate, pms_value, totalValues)
  },
  "summary": {
    "openingStockValue": 167060.0,
    "totalSupplyValue": 85000.0,
    "availableStockValue": 252060.0,
    "salesValue": 172742.14,
    "closingStockValue": 197638.0,
    "saleClosingStock": 370380.14,
    "expectedProfit": -118320.14,
    "salesProfit": 19474.61,
    "undergroundGainsLoss": 2769.31,
    "winfall": "To be implemented later",
    "shortfall": "To be implemented later",
    "advances": 5000.0,
    "credit": "To be implemented later",
    "ecash": "To be implemented later",
    "momoShortageRefund": "To be implemented later",
    "advanceRefund": 2000.0,
    "creditRefund": "To be implemented later",
    "expectedLodgement": 172742.14,
    "actualLodgement": 165000.0,
    "difference": -7742.14
  }
}
```

## Key Features Added:

### Financial Analysis
- **openingStockValue**: Total value of stock at beginning of period
- **totalSupplyValue**: Sum of all supply costs (from Supply.amountCost)
- **availableStockValue**: Combined opening stock and supply values
- **salesValue**: Total sales revenue (from DailySales.value)
- **closingStockValue**: Value of remaining stock

### Profit Calculations
- **expectedProfit**: Theoretical profit calculation
- **salesProfit**: Actual sales margin
- **saleClosingStock**: Combined sales and closing stock value

### Transaction Tracking
- **advances**: Total advances given to customers
- **advanceRefund**: Total advance repayments received
- **expectedLodgement**: Expected bank deposits
- **actualLodgement**: Actual bank deposits
- **difference**: Variance between expected and actual deposits

### Underground Storage Analysis
- **undergroundGainsLoss**: Gains or losses from underground tank measurements

### Future Implementation
Several fields are marked as "To be implemented later":
- winfall
- shortfall
- credit
- ecash
- momoShortageRefund
- creditRefund

These can be implemented as business requirements are clarified.

## Data Sources
- **DailySales**: Primary source for sales transactions, advances, banking data
- **Supply**: Source for supply costs and quantities
- **TotalValues**: Aggregated calculations from existing PMS/AGO value data

## Benefits
1. **Comprehensive Financial Overview**: Single endpoint provides complete financial picture
2. **Automated Calculations**: All summary values calculated automatically from existing data
3. **Consistency**: Uses same data sources as existing reports for accuracy
4. **Extensible**: Easy to add new calculations as requirements evolve