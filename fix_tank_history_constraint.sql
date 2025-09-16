-- Fix tank_history_operation_check constraint to allow all TankOperation enum values
-- Drop the existing constraint
ALTER TABLE tank_history DROP CONSTRAINT IF EXISTS tank_history_operation_check;

-- Create a new constraint with all valid operations
ALTER TABLE tank_history ADD CONSTRAINT tank_history_operation_check
CHECK (operation IN (
    'SUPPLY_RECEIVED',
    'FUEL_ADDED',
    'SUPPLY',
    'DISPENSED',
    'FUEL_REMOVED',
    'SALE',
    'STOCK_ADJUSTMENT',
    'ADJUSTMENT',
    'DIPPING',
    'CALIBRATION',
    'MAINTENANCE',
    'TANK_CREATED',
    'PRICE_UPDATE'
));
