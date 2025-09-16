@echo off
echo Setting up PostgreSQL database for KodsonApi...
echo.
echo Please run these commands in your PostgreSQL command prompt (psql):
echo.
echo 1. Connect to PostgreSQL as superuser:
echo    psql -U iconmaxwells
echo.
echo 2. Then run these SQL commands:
echo.
echo CREATE DATABASE kodsonplus;
echo CREATE USER kodsonuser WITH ENCRYPTED PASSWORD 'kodsonpass';
echo GRANT ALL PRIVILEGES ON DATABASE kodsonplus TO kodsonuser;
echo \c kodsonplus;
echo GRANT ALL ON SCHEMA public TO kodsonuser;
echo GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO kodsonuser;
echo GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO kodsonuser;
echo.
echo Setup complete!
pause

