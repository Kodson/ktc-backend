-- PostgreSQL Database Setup Script
-- Run this script to create the database and user for KodsonApi

-- Create database
CREATE DATABASE ktc_postgress;

-- Create user
CREATE USER ktc_kodson_user WITH ENCRYPTED PASSWORD '0040105715@Kodson12345';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE ktc_postgress TO ktc_kodson_user;

-- Connect to the database
\c ktc_postgress;

-- Grant schema privileges
GRANT ALL ON SCHEMA public TO ktc_kodson_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO ktc_kodson_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO ktc_kodson_user;

-- Create indexes for better performance (run after tables are created)
-- These will be created automatically by Hibernate, but we can optimize them

-- Example performance indexes (adjust based on your actual table structure)
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_tank_station_fuel ON tanks(station, fuel_type);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_supply_status ON supplies(status);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_daily_sales_date ON daily_sales(sale_date);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_user_username ON users(username);
-- CREATE INDEX CONCURRENTLY IF NOT EXISTS idx_customer_phone ON customers(phone_number);
