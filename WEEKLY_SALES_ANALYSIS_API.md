# Weekly Sales Analysis API Documentation

## Overview
The Weekly Sales Analysis API provides detailed weekly sales breakdowns that replicate the Excel-style analysis shown in your requirements. This endpoint analyzes current month sales data by segregating weeks based on a reference date from the previous month's last week.

## Endpoint

### GET `/api/reports/weekly-analysis/{stationName}`

**Description:** Generates weekly sales analysis for a specific station

**Parameters:**
- `stationName` (path parameter): Name of the station to analyze
- `lastMonthLastWeekDate` (query parameter): Reference date from the last week of the previous month (format: YYYY-MM-DD)

**Example Request:**
```
GET /api/reports/weekly-analysis/STATION_A?lastMonthLastWeekDate=2025-07-29
```

## Response Structure

```json
{
  "stationName": "STATION_A",
  "referenceDate": "2025-07-29",
  "currentMonth": "AUGUST",
  "weeklyAnalysis": [
    {
      "month": "AUGUST",
      "week": "WEEK 1",
      "timePeriod": "30th - 5th",
      "salesLtrs": 7464.38,
      "pms": 5589.55,
      "ago": 1874.83,
      "diffPms": 5589.55,
      "diffAgo": 1874.83,
      "differenceLtrs": 7464.38,
      "percentageChange": "100.00%"
    },
    {
      "month": "AUGUST",
      "week": "WEEK 2",
      "timePeriod": "6th - 11th",
      "salesLtrs": 5744.81,
      "pms": 4209.99,
      "ago": 1534.82,
      "diffPms": -1379.56,
      "diffAgo": -340.01,
      "differenceLtrs": -1719.57,
      "percentageChange": "-29.93%"
    }
    // ... more weeks
  ]
}
```

## Field Descriptions

### Response Fields
- **stationName**: Name of the analyzed station
- **referenceDate**: The reference date used for week calculation
- **currentMonth**: Month being analyzed
- **weeklyAnalysis**: Array of weekly data objects

### Weekly Data Fields
- **month**: Month name (e.g., "AUGUST")
- **week**: Week identifier (e.g., "WEEK 1", "WEEK 2")
- **timePeriod**: Date range for the week (e.g., "30th - 5th")
- **salesLtrs**: Total sales in liters for the week (sum of all salesL from DailySales)
- **pms**: PMS sales in liters for the week (salesL where product = "PMS")
- **ago**: AGO sales in liters for the week (salesL where product = "AGO")
- **diffPms**: Difference in PMS sales compared to previous week
- **diffAgo**: Difference in AGO sales compared to previous week
- **differenceLtrs**: Difference in total sales compared to previous week
- **percentageChange**: Percentage change in total sales compared to previous week

## Calculation Logic

### Week Segregation
1. Takes the `lastMonthLastWeekDate` as reference point
2. Calculates current month weeks starting from the day after the reference date
3. Each week spans 7 days, with adjustments for month boundaries
4. Automatically handles different month lengths

### Sales Calculations
- **salesLtrs**: `SUM(DailySales.salesL)` for the week period
- **pms**: `SUM(DailySales.salesL WHERE product = 'PMS')` for the week
- **ago**: `SUM(DailySales.salesL WHERE product = 'AGO')` for the week

### Difference Calculations
- **diffPms**: `current_week_pms - previous_week_pms`
- **diffAgo**: `current_week_ago - previous_week_ago`
- **differenceLtrs**: `current_week_total - previous_week_total`

### Percentage Change
- **percentageChange**: `(differenceLtrs / previous_week_total) * 100`
- First week uses 100.00% as baseline (no previous week to compare)

## Example Usage Scenarios

### Scenario 1: August Analysis
If you want to analyze August 2025 sales:
- Use July's last week end date as reference: `2025-07-29`
- The API will calculate August weeks starting from July 30th

### Scenario 2: September Analysis  
If you want to analyze September 2025 sales:
- Use August's last week end date as reference: `2025-08-31`
- The API will calculate September weeks starting from September 1st

## Data Source
- **Primary**: `DailySales` entity
- **Fields Used**: `station`, `date`, `product`, `salesL`
- **Filtering**: By station name and date range for each week

## Features
- ✅ Automatic week boundary calculation
- ✅ Cross-month week handling
- ✅ Product-specific breakdowns (PMS vs AGO)
- ✅ Week-over-week comparison
- ✅ Percentage change calculations
- ✅ Proper date formatting with ordinal suffixes
- ✅ Comprehensive logging for debugging

## Error Handling
- Invalid date formats return clear error messages
- Missing station data returns empty analysis
- Handles edge cases like month boundaries automatically
- Graceful handling of missing sales data

## Performance Considerations
- Fetches all station data once and filters in memory
- Optimized for monthly analysis (typically 4-5 weeks)
- Includes safety checks to prevent infinite loops
- Efficient stream processing for calculations