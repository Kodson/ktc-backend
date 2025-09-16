# KodsonApi - Refactored Backend Application

## Overview
This is a comprehensive refactoring of the KodsonApi backend application with the following improvements:
- Migrated from MySQL to PostgreSQL for better performance and scalability
- Removed OTP verification features (as requested)
- Implemented proper route protection with role-based access control
- Added Redis caching for improved performance
- Implemented comprehensive error handling and validation
- Added performance monitoring and logging
- Updated to latest Spring Boot security practices

## Technology Stack
- **Java 21**
- **Spring Boot 3.2.0**
- **PostgreSQL** (migrated from MySQL)
- **Redis** (for caching)
- **Spring Security** (with JWT authentication)
- **Spring Data JPA** (with Hibernate)
- **Kafka** (for event streaming)
- **HikariCP** (for connection pooling)

## Prerequisites
1. Java 21 or higher
2. Maven 3.8+
3. PostgreSQL 14+ 
4. Redis 6+ (optional, for caching)
5. Docker (optional, for containerized deployment)

## Database Setup

### PostgreSQL Installation and Configuration
1. Install PostgreSQL and create the database:
```sql
-- Run the script in src/main/resources/db/migration/postgresql-setup.sql
CREATE DATABASE kodsonplus;
CREATE USER kodsonuser WITH ENCRYPTED PASSWORD 'kodsonpass';
GRANT ALL PRIVILEGES ON DATABASE kodsonplus TO kodsonuser;
```

2. Update the database connection in `application.yaml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/kodsonplus
    username: kodsonuser
    password: kodsonpass
```

### Redis Setup (Optional but Recommended)
1. Install and start Redis server
2. Default configuration works with localhost:6379

## Installation and Setup

1. **Clone and navigate to project:**
```bash
cd "D:\KTC App\KodsonApi"
```

2. **Install dependencies:**
```bash
mvn clean install
```

3. **Run database migrations:**
The application will automatically create tables on first run with the new PostgreSQL configuration.

4. **Start the application:**
```bash
mvn spring-boot:run
```

Or for production:
```bash
java -jar target/serve-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
```

## Configuration Profiles

### Development (`application-dev.yml`)
- Uses development database
- Detailed logging enabled
- Auto-creates database tables
- Lower thread pool limits

### Production (`application-prod.yml`)
- Uses environment variables for configuration
- Optimized logging
- SSL support
- Validates database schema only

## API Security and Authentication

### Default Admin Users
The application automatically creates default admin users on first startup through the `DataInitializationService`:

#### Super Admin User
- **Username**: `I.T`
- **Email**: `iconmaxwellsowusu@gmail.com`
- **Password**: `0040105715@Icon`
- **Role**: `ROLE_SUPER_ADMIN`
- **Status**: Active, Account Verified, Not Locked
- **Phone**: `+233408527275`

#### GM Admin User
- **Username**: `GM`
- **Email**: `gm@kodsonplusltd.com`
- **Password**: `GM@Admin2024`
- **Role**: `ROLE_GM`
- **Status**: Active, Account Verified, Not Locked
- **Phone**: `+233244567891`

#### FAM Admin User
- **Username**: `FAM`
- **Email**: `fam@kodsonplusltd.com`
- **Password**: `FAM@Admin2024`
- **Role**: `ROLE_FAM`
- **Status**: Active, Account Verified, Not Locked
- **Phone**: `+233244567890`

**Note**: These users are created automatically when the application starts if they don't already exist. All passwords are encrypted using BCrypt. MFA is disabled for these admin accounts for easier access.

### Role-Based Access Control
The application implements comprehensive role-based security:

- **ADMIN**: Full access to all endpoints
- **MANAGER**: Access to management and reporting functions
- **ATTENDANT**: Access to operational endpoints (sales, tanks)
- **USER**: Limited access to customer-related functions

### Protected Endpoints
- `/api/admin/**` - Admin only
- `/api/reports/**` - Manager and above
- `/api/supplies/**` - Manager and above
- `/api/tanks/**` - Manager and Attendant
- `/api/sales/**` - All authenticated users

### JWT Authentication
- JWT tokens are required for all protected endpoints
- Tokens include user roles and are cached for performance
- Token expiration is set to 5 days
- Proper token validation with secure signing

## New Features and Endpoints

### Tank Management
- `GET /api/tanks/{id}/history` - Get tank operation history
- `PUT /api/tanks/{id}/stock` - Update tank stock levels
- Automatic history tracking for all tank operations

### Supply Management
- `POST /api/supplies/bulk` - Create multiple supplies
- `PUT /api/supplies/{id}/approve` - Approve supply requests
- `PUT /api/supplies/{id}/reject` - Reject supply requests  
- `PUT /api/supplies/{id}/confirm` - Confirm supply receipt
- `GET /api/supplies/pending` - Get pending supplies
- Automatic tank stock updates on supply confirmation

### Daily Sales Validation
- `PUT /api/daily-sales/{id}/validate` - Validate daily sales entries
- `GET /api/daily-sales/pending` - Get pending validations
- `GET /api/daily-sales/status/{status}` - Filter by validation status

## Performance Optimizations

### Caching Strategy
- Redis-based caching for frequently accessed data
- Method-level caching for expensive operations
- Cache invalidation on data updates

### Database Optimizations
- HikariCP connection pooling (max 20 connections)
- Hibernate batch processing enabled
- Query optimization with proper indexing
- Connection timeout and idle management

### Application Performance
- Async processing for non-blocking operations
- Optimized Jackson JSON serialization
- Reduced file upload limits (100MB vs 1GB)
- Performance monitoring with metrics

## Monitoring and Health Checks

### Actuator Endpoints
- `/actuator/health` - Application health status
- `/actuator/metrics` - Performance metrics
- `/actuator/prometheus` - Prometheus metrics export

### Logging
- Structured logging with appropriate levels
- Performance monitoring for slow operations
- Error tracking and reporting
- Log file rotation (100MB, 30 days retention)

## Error Handling
- Global exception handler with proper HTTP status codes
- Validation error responses with field-level details
- Security error handling
- Database constraint violation handling

## Migration from MySQL to PostgreSQL

### Data Migration Steps
1. Export existing MySQL data
2. Update entity annotations for PostgreSQL compatibility
3. Run the PostgreSQL setup script
4. Import data with proper type conversions
5. Update application configuration

### Breaking Changes
- Database dialect changed to PostgreSQL
- Some MySQL-specific queries may need updates
- Connection pool configuration optimized for PostgreSQL

## Testing
```bash
# Run all tests
mvn test

# Run with specific profile
mvn test -Dspring.profiles.active=test
```

## Docker Deployment (Optional)
```dockerfile
# Build image
docker build -t kodson-api .

# Run with environment variables
docker run -d \
  -e DATABASE_URL=jdbc:postgresql://db:5432/kodsonplus \
  -e DATABASE_USERNAME=kodsonuser \
  -e DATABASE_PASSWORD=kodsonpass \
  -e REDIS_HOST=redis \
  -p 8081:8081 \
  kodson-api
```

## Security Best Practices Implemented
- HTTPS support in production
- Security headers (HSTS, XSS Protection, etc.)
- CORS configuration
- JWT token security improvements
- Input validation and sanitization
- SQL injection prevention
- Rate limiting considerations

## Performance Monitoring
- Method execution time tracking
- Slow query detection
- Memory usage monitoring
- Connection pool monitoring
- Cache hit/miss ratios

## Troubleshooting

### Common Issues
1. **Database Connection**: Ensure PostgreSQL is running and credentials are correct
2. **Redis Connection**: Redis is optional but recommended for optimal performance
3. **JWT Errors**: Check token expiration and signing key configuration
4. **Permission Errors**: Verify user roles and endpoint security configuration

### Log Locations
- Development: Console output
- Production: `logs/kodson-api.log`

## API Documentation
Once the application is running, API documentation is available at:
- Swagger UI: `http://localhost:8081/swagger-ui.html` (if enabled)
- Actuator endpoints: `http://localhost:8081/actuator`

## Support and Maintenance
- Regular dependency updates recommended
- Monitor logs for performance issues
- Database maintenance and backup procedures
- Cache monitoring and cleanup
