# Daily Sales Target Date Enhancement

## Overview

The `getLatestDailySalesByStationAndProduct` function has been enhanced to support target date queries, allowing users to retrieve the most recent sales data on or before a specified date, rather than always returning the absolute latest record.

## New Features

### 1. Target Date Support
- Find the most recent sales record on or before a specified target date
- Supports various date input formats
- Returns comprehensive sales data with cashToBank information

### 2. Product-Specific Data Analysis
- For the selected date, checks for both PMS and AGO product data
- Returns cashToBank values for available products on that date
- Provides complete product information in the response

## API Endpoints

### Get Latest Sales by Target Date (String Date)
```
GET /api/dailysales/latest-by-date/{station}/{product}?targetDate=2024-12-14
```

**Parameters:**
- `station`: Station name
- `product`: Product name
- `targetDate`: Date in format "yyyy-MM-dd"

### Get Latest Sales by Target DateTime
```
GET /api/dailysales/latest-by-date-time/{station}/{product}?targetDateTime=2024-12-14T10:30:00
```

**Parameters:**
- `station`: Station name  
- `product`: Product name
- `targetDateTime`: DateTime in ISO format "yyyy-MM-ddTHH:mm:ss"

## Response Format

The API returns a `DailySalesWithCashToBankResponse` object containing:

```json
{
  "id": "record-uuid",
  "station": "Station A",
  "date": "2024-12-14",
  "product": "PMS",
  "salesL": 1500.5,
  "rate": 650.0,
  "value": 975325.0,
  "cashToBank": 850000.0,
  "createdAt": "2024-12-14T09:15:30",
  "targetDateTime": "2024-12-14T23:59:59",
  "productData": {
    "PMS": {
      "productName": "PMS",
      "cashToBank": 850000.0,
      "salesL": 1500.5,
      "value": 975325.0,
      "recordDate": "2024-12-14T09:15:30",
      "recordId": "pms-record-uuid"
    },
    "AGO": {
      "productName": "AGO",
      "cashToBank": 620000.0,
      "salesL": 1200.0,
      "value": 780000.0,
      "recordDate": "2024-12-14T09:20:15",
      "recordId": "ago-record-uuid"
    }
  }
}
```

## Service Methods

### New Service Methods

1. **getLatestDailySalesByStationAndProductByDate(String station, String product, LocalDateTime targetDate)**
   - Core method accepting LocalDateTime for precise control

2. **getLatestDailySalesByStationAndProductByDate(String station, String product, String targetDateStr)**
   - Convenience method accepting string dates in format "yyyy-MM-dd" or "yyyy-MM-dd HH:mm:ss"

3. **getLatestDailySalesByStationAndProductByDate(String station, String product, LocalDate targetDate)**
   - Convenience method for LocalDate (uses end of day as target time)

### Repository Enhancements

New repository methods added:
- `findFirstByStationAndProductAndCreatedAtLessThanEqualOrderByCreatedAtDesc`
- `findByStationAndCreatedAtLessThanEqualOrderByCreatedAtDesc` 
- `findByStationAndProductInAndCreatedAtLessThanEqualOrderByCreatedAtDesc`

## Use Cases

### Example 1: Get Sales Data as of Specific Date
If you have sales data for 10 consecutive days (Dec 1-10) and want data as of Dec 5:
- The function returns the most recent record on or before Dec 5
- Includes cashToBank information for both PMS and AGO if available on that date

### Example 2: Historical Analysis
- Analyze sales performance at any point in time
- Compare cashToBank values across different products on the same date
- Track sales progression over time

## Error Handling

- **EntityNotFoundException**: Thrown when no sales data exists for the specified criteria
- **IllegalArgumentException**: Thrown for invalid date formats
- Comprehensive logging for debugging and audit trails

## Caching

All new methods are cached using Spring's caching mechanism with appropriate cache keys:
- `latest-by-date-{station}-{product}-{targetDate}`
- `latest-by-string-date-{station}-{product}-{targetDateStr}`
- `latest-by-local-date-{station}-{product}-{targetDate}`

## Performance Considerations

- Database queries are optimized with proper indexing on `station`, `product`, and `createdAt` fields
- Caching reduces database load for frequently accessed data
- Response includes only essential product data to minimize payload size